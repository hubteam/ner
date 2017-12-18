package com.wxw.ner.character.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxw.namedentity.NamedEntity;
import com.wxw.ner.character.feature.NERCharacterContextGenerator;
import com.wxw.ner.event.NERCharacterSampleEvent;
import com.wxw.ner.sample.AbstractNERSample;
import com.wxw.ner.sample.FileInputStreamFactory;
import com.wxw.ner.sample.NERCharacterSample;
import com.wxw.ner.sample.NERCharacterSampleStream;
import com.wxw.ner.sequence.DefaultNERCharacterSequenceValidator;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.maxent.io.PlainTextGISModelReader;
import opennlp.tools.ml.maxent.io.PlainTextGISModelWriter;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.TrainingParameters;

/**
 * 为基于字的命名实体识别训练模型
 * @author 王馨苇
 *
 */
public class NERCharacterME implements NERCharacter{

	public static final int DEFAULT_BEAM_SIZE = 3;
	private NERCharacterContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private SequenceClassificationModel<String> model;
	private NERCharacterModel modelPackage;
    private SequenceValidator<String> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public NERCharacterME(NERCharacterModel model, NERCharacterContextGenerator contextGen) {
		init(model , contextGen);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	private void init(NERCharacterModel model, NERCharacterContextGenerator contextGen) {
		int beamSize = NERCharacterME.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

        modelPackage = model;

        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultNERCharacterSequenceValidator();
        if (model.getNERSequenceModel() != null) {
            this.model = model.getNERSequenceModel();
        } else {
            this.model = new BeamSearch<String>(beamSize,
                    model.getNERModel(), 0);
        }
	}
	
	/**
	 * 训练模型
	 * @param file 训练文件
	 * @param params 训练
	 * @param contextGen 特征
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static NERCharacterModel train(File file, TrainingParameters params, NERCharacterContextGenerator contextGen,
			String encoding){
		NERCharacterModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<AbstractNERSample> sampleStream = new NERCharacterSampleStream(lineStream);
			model = NERCharacterME.train("zh", sampleStream, params, contextGen);
			return model;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}

	/**
	 * 训练模型
	 * @param languageCode 编码
	 * @param sampleStream 文件流
	 * @param contextGen 特征
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static NERCharacterModel train(String languageCode, ObjectStream<AbstractNERSample> sampleStream, TrainingParameters params,
			NERCharacterContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = NERCharacterME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel posModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        //event_model_trainer
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        SequenceClassificationModel<String> seqPosModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
        	//sampleStream为PhraseAnalysisSampleStream对象
            ObjectStream<Event> es = new NERCharacterSampleEvent(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
            posModel = trainer.train(es);                       
        }

        if (posModel != null) {
            return new NERCharacterModel(languageCode, posModel, beamSize, manifestInfoEntries);
        } else {
            return new NERCharacterModel(languageCode, seqPosModel, manifestInfoEntries);
        }
	}

	/**
	 * 训练模型，并将模型写出
	 * @param file 训练的文本
	 * @param modelbinaryFile 二进制的模型文件
	 * @param modeltxtFile 文本类型的模型文件
	 * @param params 训练的参数配置
	 * @param contextGen 上下文 产生器
	 * @param encoding 编码方式
	 * @return
	 */
	public static NERCharacterModel train(File file, File modelbinaryFile, TrainingParameters params,
			NERCharacterContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		NERCharacterModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<AbstractNERSample> sampleStream = new NERCharacterSampleStream(lineStream);
			model = NERCharacterME.train("zh", sampleStream, params, contextGen);
			 //模型的持久化，写出的为二进制文件
            modelOut = new BufferedOutputStream(new FileOutputStream(modelbinaryFile));           
            model.serialize(modelOut);
            return model;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
            if (modelOut != null) {
                try {
                    modelOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }	
		return null;
	}
	
	public String[] tag(String[] characters,Object[] additionaContext){
		bestSequence = model.bestSequence(characters, additionaContext, contextGenerator,sequenceValidator);
		List<String> t = bestSequence.getOutcomes();
		return t.toArray(new String[t.size()]);
	}
	/**
	 * 根据训练得到的模型文件得到
	 * @param modelFile 模型文件
	 * @param params 参数
	 * @param contextGen 上下文生成器
	 * @param encoding 编码方式
	 * @return
	 */
	public static NERCharacterModel readModel(File modelFile, TrainingParameters params, NERCharacterContextGenerator contextGen,
			String encoding) {
		PlainTextGISModelReader modelReader = null;
		AbstractModel abModel = null;
		NERCharacterModel model = null;
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
	      
        int beamSize = NERCharacterME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

		try {
			Map<String, String> manifestInfoEntries = new HashMap<String, String>();
			modelReader = new PlainTextGISModelReader(modelFile);			
			abModel = modelReader.getModel();
			model =  new NERCharacterModel(encoding, abModel, beamSize,manifestInfoEntries);
	
			System.out.println("读取模型成功");
            return model;
        } catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 生语料转换成字符串数组
	 * @param sentence 读入的生语料
	 * @return
	 */
    public String[] tocharacters(String sentence) {
        String[] chars = new String[sentence.length()];
        for (int i = 0; i < sentence.length(); i++) {
            chars[i] = sentence.charAt(i) + "";
        }
        return chars;
    }
    
    /**
	 * 得到最好的numTaggings个标记序列
	 * @param numTaggings 个数
	 * @param characters 一个个字
	 * @return 分词加词性标注的序列
	 */
	public String[][] tag(int numTaggings, String[] characters) {
        Sequence[] bestSequences = model.bestSequences(numTaggings, characters, null,
        		contextGenerator, sequenceValidator);
        String[][] tagsandposes = new String[bestSequences.length][];
        for (int si = 0; si < tagsandposes.length; si++) {
            List<String> t = bestSequences[si].getOutcomes();
            tagsandposes[si] = t.toArray(new String[t.size()]);
           
        }
        return tagsandposes;
    }

	/**
	 * 最好的K个序列
	 * @param characters 一个个字
	 * @return
	 */
    public Sequence[] topKSequences(String[] characters) {
        return this.topKSequences(characters, null);
    }

    /**
     * 最好的K个序列
     * @param characters 一个个字
     * @param additionaContext
     * @return 
     */
    public Sequence[] topKSequences(String[] characters, Object[] additionaContext) {
        return model.bestSequences(size, characters, additionaContext,
        		contextGenerator, sequenceValidator);
    }
    
    /**
	   * 返回一个ner实体
	   * @param begin 开始位置
	   * @param tags 标记序列
	   * @param words 词语序列
	   * @param flag 实体标记
	   * @return
	   */
	public NamedEntity getNer(int begin,String[] tags,String[] words,String flag){
		NamedEntity ner = new NamedEntity();
		for (int i = begin; i < tags.length; i++) {
			List<String> wordStr = new ArrayList<>();
			String word = "";
			if(tags[i].equals(flag)){
				ner.setStart(i);
				word += words[i];
				wordStr.add(words[i]);
				for (int j = i+1; j < tags.length; j++) {
					if(tags[j].equals(flag)){
						word += words[j];
						wordStr.add(words[j]);
						if(j == tags.length-1){
							ner.setString(word);
							ner.setType(flag);
							ner.setWords(wordStr.toArray(new String[wordStr.size()]));
							ner.setEnd(j);
							break;
						}
					}else{
						ner.setString(word);
						ner.setType(flag);
						ner.setWords(wordStr.toArray(new String[wordStr.size()]));
						ner.setEnd(j-1);
						break;
					}
				}
			}else if(tags[i].split("_")[1].equals(flag) && tags[i].split("_")[0].equals("b")){
				ner.setStart(i);
				word += words[i];
				wordStr.add(words[i]);
				for (int j = i+1; j < tags.length; j++) {
					word += words[j];
					wordStr.add(words[j]);
					if(tags[j].split("_")[1].equals(flag) && tags[j].split("_")[0].equals("m")){
							
					}else if(tags[j].split("_")[1].equals(flag) && tags[j].split("_")[0].equals("e")){
						ner.setString(word);
						ner.setType(flag);
						ner.setWords(wordStr.toArray(new String[wordStr.size()]));
						ner.setEnd(j);
						break;
					}
				}
			}else{
				if(tags[i].split("_")[1].equals(flag) && tags[i].split("_")[0].equals("s")){
					ner.setStart(i);
					word += words[i];
					wordStr.add(words[i]);
					ner.setString(word);
					ner.setType(flag);
					ner.setWords(wordStr.toArray(new String[wordStr.size()]));
					ner.setEnd(i);
					break;
				}
			}
			break;
		}
		return ner;
	}
	
    /**
	 * 读入一段单个字组成的语料
	 * @param sentence 单个字组成的数组
	 * @return
	 */
	@Override
	public NamedEntity[] ner(String[] sentence) {
		String[] tags = tag(sentence,null);
		List<NamedEntity> ners = new ArrayList<>();
		for (int i = 0; i < tags.length; i++) {
			String flag;
			if(tags[i].equals("o")){
				flag = "o";
			}else{
				flag = tags[i].split("_")[1];
			}
			if(ners.size() == 0){
				ners.add(getNer(0,tags,sentence,flag));
			}else{
				ners.add(getNer(i,tags,sentence,flag));
			}
			i = ners.get(ners.size()-1).getEnd();
		}
 		return ners.toArray(new NamedEntity[ners.size()]);
	} 
	
	
	/**
	 * 得到命名实体识别的结果
	 */
	@Override
	public NamedEntity[] ner(String sentence) {
		String[] tags = tocharacters(sentence);
        return ner(tags);
	}
	/**
	 * 读入一句生语料，进行标注，得到指定的命名实体
	 * @param sentence 读取的生语料
	 * @param flag 命名实体标记
	 * @return
	 */
	@Override
	public NamedEntity[] ner(String sentence, String flag) {
		String[] tags = tocharacters(sentence);
        return ner(tags,flag);
	}
	/**
	 * 读入一段单个字组成的语料,得到指定的命名实体
	 * @param sentence 单个字组成的数组
	 * @param flag 命名实体标记
	 * @return
	 */
	@Override
	public NamedEntity[] ner(String[] sentence, String flag) {
		NamedEntity[] ners = ner(sentence);
		for (int i = 0; i < ners.length; i++) {
			if(ners[i].getType().equals(flag)){
				
			}else{
				ners[i].setType("o");
			}
		}
 		return ners;
	}
	/**
	 * 读入一句生语料，进行标注，得到最好的K个结果
	 * @param sentence 读取的生语料
	 * @return
	 */
	public NamedEntity[][] ner(int k,String sentence){
		String[] tags = tocharacters(sentence);
        return ner(k,tags);
	}
	
	/**
	 * 读入一段单个字组成的语料,得到最好的K个结果
	 * @param sentence 单个字组成的数组
	 * @return
	 */
	public NamedEntity[][] ner(int k,String[] sentence){
		String[][] tags = tag(k,sentence);
		NamedEntity[][] kners = new NamedEntity[k][];
		for (int i = 0; i < tags.length; i++) {
			List<NamedEntity> ners = new ArrayList<>();
			for (int j = 0; j < tags[i].length; j++) {
				String flag;
				if(tags[j].equals("o")){
					flag = "o";
				}else{
					flag = tags[i][j].split("_")[1];
				}
				if(ners.size() == 0){
					ners.add(getNer(0,tags[i],sentence,flag));
				}else{
					ners.add(getNer(j,tags[i],sentence,flag));
				}
				j = ners.get(ners.size()-1).getEnd();
			}
			kners[i] = ners.toArray(new NamedEntity[ners.size()]);
		}
 		return kners;
	}
}

