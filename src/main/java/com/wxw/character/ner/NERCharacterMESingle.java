package com.wxw.character.ner;

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
import com.wxw.ner.sample.NERWordOrCharacterSample;
import com.wxw.ner.sample.FileInputStreamFactory;

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
 * 为基于字，且一次只训练一种命名实体训练模型
 * @author 王馨苇
 *
 */
public class NERCharacterMESingle implements NERCharacterSingle{

	public static final int DEFAULT_BEAM_SIZE = 3;
	private NERCharacterContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private SequenceClassificationModel<String> model;
	private NERCharacterModel modelPackage;
	private List<String> characters = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

    private SequenceValidator<String> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public NERCharacterMESingle(NERCharacterModel model, NERCharacterContextGenerator contextGen) {
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
			ObjectStream<NERWordOrCharacterSample> sampleStream = new NERCharacterSampleStreamSingle(lineStream);
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
	public static NERCharacterModel train(String languageCode, ObjectStream<NERWordOrCharacterSample> sampleStream, TrainingParameters params,
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
	public static NERCharacterModel train(File file, File modelbinaryFile, File modeltxtFile, TrainingParameters params,
			NERCharacterContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		PlainTextGISModelWriter modelWriter = null;
		NERCharacterModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERWordOrCharacterSample> sampleStream = new NERCharacterSampleStreamSingle(lineStream);
			model = NERCharacterME.train("zh", sampleStream, params, contextGen);
			 //模型的持久化，写出的为二进制文件
            modelOut = new BufferedOutputStream(new FileOutputStream(modelbinaryFile));           
            model.serialize(modelOut);
            //模型的写出，文本文件
            modelWriter = new PlainTextGISModelWriter((AbstractModel) model.getNERModel(), modeltxtFile);
            modelWriter.persist();
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
	
	/**
	 * 标记序列
	 * @param characters 字符序列
	 * @param additionaContext
	 * @return
	 */
	public String[] tag(String[] characters,Object[] additionaContext){
		bestSequence = model.bestSequence(characters, additionaContext, contextGenerator,sequenceValidator);
      //  System.out.println(bestSequence);
		List<String> t = bestSequence.getOutcomes();
		return t.toArray(new String[t.size()]);
//		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	 /**
	  * 读入一段单个字组成的语料
	  * @param sentence 单个字组成的数组
      * @return
	  */		
	@Override	
	public NamedEntity[] ner(String[] sentence,String flag) {		
		String[] tags = tag(sentence,null);
		List<NamedEntity> ners = new ArrayList<>();
		for (int i = 0; i < tags.length; i++) {
			if(ners.size() == 0){
				ners.add(getNer(0,tags,sentence,tags[0].split("_")[0]));
			}else{
				ners.add(getNer(ners.get(ners.size()-1).getEnd()+1,tags,sentence,
						tags[ners.get(ners.size()-1).getEnd()+1].split("_")[0]));
			}
			i = ners.get(ners.size()-1).getEnd();
		}
 		return ners.toArray(new NamedEntity[ners.size()]);
	} 
		
	/**
	 * 得到命名实体识别的结果
	 */
	@Override
	public NamedEntity[] ner(String sentence,String flag) {	
		String[] tags = tocharacters(sentence);   
		return ner(tags,flag);
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
			if(tags[i].split("_")[0].equals(flag)){
				ner.setStart(i);
				word += words[i];
				wordStr.add(words[i]);
				for (int j = i+1; j < tags.length; j++) {
					if(tags[j].split("_")[0].equals(flag)){
						word += words[j];
						wordStr.add(words[j]);
					}else{
						ner.setString(word);
						ner.setType(flag);
						ner.setWords(wordStr.toArray(new String[wordStr.size()]));
						ner.setEnd(j-1);
						break;
					}
				}
			}
			break;
		}
		return ner;
	}
}

