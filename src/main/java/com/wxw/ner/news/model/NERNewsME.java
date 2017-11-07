package com.wxw.ner.news.model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.wxw.ner.event.NERNewsSampleEvent;
import com.wxw.ner.news.feature.NERNewsContextGenerator;
import com.wxw.ner.sample.FileInputStreamFactory;
import com.wxw.ner.sample.NERNewsSample;
import com.wxw.ner.sample.NERNewsSampleStream;
import com.wxw.ner.sequence.DefaultNERNewsSequenceValidator;
import com.wxw.ner.sequence.NERBeamSearch;
import com.wxw.ner.sequence.NERSequenceClassificationModel;
import com.wxw.ner.sequence.NERSequenceValidator;

import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.maxent.io.PlainTextGISModelReader;
import opennlp.tools.ml.maxent.io.PlainTextGISModelWriter;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.TrainingParameters;

/**
 * 训练模型，标记序列
 * @author 王馨苇
 *
 */
public class NERNewsME implements NERNews{
	public static final int DEFAULT_BEAM_SIZE = 3;
	private NERNewsContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private NERSequenceClassificationModel<String> model;
	private NERNewsModel modelPackage;
	private List<String> characters = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

    private NERSequenceValidator<String> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public NERNewsME(NERNewsModel model, NERNewsContextGenerator contextGen) {
		init(model , contextGen);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	private void init(NERNewsModel model, NERNewsContextGenerator contextGen) {
		int beamSize = NERNewsME.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(NERBeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

        modelPackage = model;

        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultNERNewsSequenceValidator();
        if (model.getNERSequenceModel() != null) {
            this.model = model.getNERSequenceModel();
        } else {
            this.model = new NERBeamSearch<String>(beamSize,
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
	public static NERNewsModel train(File file, TrainingParameters params, NERNewsContextGenerator contextGen,
			String encoding){
		NERNewsModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERNewsSample> sampleStream = new NERNewsSampleStream(lineStream);
			model = NERNewsME.train("zh", sampleStream, params, contextGen);
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
	public static NERNewsModel train(String languageCode, ObjectStream<NERNewsSample> sampleStream, TrainingParameters params,
			NERNewsContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(NERBeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = NERNewsME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel posModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        //event_model_trainer
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        NERSequenceClassificationModel<String> seqPosModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
        	//sampleStream为PhraseAnalysisSampleStream对象
            ObjectStream<Event> es = new NERNewsSampleEvent(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
            posModel = trainer.train(es);                       
        }

        if (posModel != null) {
            return new NERNewsModel(languageCode, posModel, beamSize, manifestInfoEntries);
        } else {
            return new NERNewsModel(languageCode, seqPosModel, manifestInfoEntries);
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
	public static NERNewsModel train(File file, File modelbinaryFile, File modeltxtFile, TrainingParameters params,
			NERNewsContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		PlainTextGISModelWriter modelWriter = null;
		NERNewsModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERNewsSample> sampleStream = new NERNewsSampleStream(lineStream);
			model = NERNewsME.train("zh", sampleStream, params, contextGen);
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
	
	public String[] tag(String[] words,String[] pos, Object[] additionaContext){
		bestSequence = model.bestSequence(words, pos, additionaContext, contextGenerator,sequenceValidator);
      //  System.out.println(bestSequence);
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
	public static NERNewsModel readModel(File modelFile, TrainingParameters params, NERNewsContextGenerator contextGen,
			String encoding) {
		PlainTextGISModelReader modelReader = null;
		AbstractModel abModel = null;
		NERNewsModel model = null;
		String beamSizeString = params.getSettings().get(NERBeamSearch.BEAM_SIZE_PARAMETER);
	      
        int beamSize = NERNewsME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

		try {
			Map<String, String> manifestInfoEntries = new HashMap<String, String>();
			modelReader = new PlainTextGISModelReader(modelFile);			
			abModel = modelReader.getModel();
			model =  new NERNewsModel(encoding, abModel, beamSize,manifestInfoEntries);
	
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
	 * 为了人名实体的解析生成的后缀语料
	 * @param samples
	 * @return
	 * @throws IOException
	 */
	public static HashSet<String> bulidDictionary(ObjectStream<NERNewsSample> samples) throws IOException {
		HashSet<String> dict = new HashSet<String>();
        NERNewsSample sample = null;
        while((sample = samples.read()) != null)
        {
            String[] words = sample.getWords();
            String[] poses = sample.getPoses();
            
            for (int i = 1; i < words.length; i++) {
//            	System.out.print(words[i]);
            	if(i == 1){
            		if(poses[i-1].equals("nr") && poses[i].equals("n")){
            			dict.add(words[i]);
    				}
            	}else{
            		if(!(poses[i-2].equals("nr")) && poses[i-1].equals("nr") && poses[i].equals("n")){
            			dict.add(words[i]);
    				}
            	}				
			}
            System.out.println();
        }        
        return dict;
	}
	
	public static HashSet<String> buildDictionary(File corpusFile, String encoding) throws IOException {
        BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(corpusFile), encoding));
        String sentence;
        HashSet<String> dict = new HashSet<String>();
        while ((sentence = data.readLine()) != null) {
            String words[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
            for (int i = 1; i < words.length; i++) {
				String word = words[i].split("/")[0];
				String pos = words[i].split("/")[1];
				if(i == 2){
            		if(words[i-1].split("/")[1].equals("nr") && pos.equals("n") && words[i-1].split("/")[0].length()<=2){
            			dict.add(word);
    				}
            	}else if(i>2){
            		if(!(words[i-2].split("/")[1].equals("nr")) && words[i-1].split("/")[1].equals("nr") 
            				&& pos.equals("n") && words[i-1].split("/")[0].length()<=2){
            			dict.add(word);
    				}
            	}
			}
        }

        data.close();

        return dict;
    }
	/**
	 * 读入一句词性标注的语料，得到最终结果
	 * @param sentence 读取的生语料
	 * @return
	 */
	@Override
	public String[] ner(String sentence) {
		String[] str = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
		List<String> words = new ArrayList<>();
		List<String> tags = new ArrayList<>();
		for (int i = 0; i < str.length; i++) {
			String word = str[i].split("/")[0];
			String tag = str[i].split("/")[1];
			words.add(word);
			tags.add(tag);
		}
		return ner(words.toArray(new String[words.size()]),
				tags.toArray(new String[tags.size()]));
	}
	/**
	 * 读入词性标注的语料，得到命名实体
	 * @param words 词语
	 * @param poses 词性
	 * @return
	 */
	@Override
	public String[] ner(String[] words, String[] poses) {
		String[] tags = tag(words,poses,null);
		String[] ner = NERNewsSample.toPos(tags);
		String[] word = NERNewsSample.toWord(words, tags);
		String[] output = null;
		for (int i = 0; i < ner.length; i++) {
			output[i] = word[i]+"/"+ner[i];
		}
		return output;
	}

	/**
	 * 得到最好的numTaggings个标记序列
	 * @param numTaggings 个数
	 * @param characters 一个个词语
	 * @param pos 词性标注
	 * @return 分词加词性标注的序列
	 */
	public String[][] tag(int numTaggings, String[] characters,String[] pos) {
        Sequence[] bestSequences = model.bestSequences(numTaggings, characters, pos, null,
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
	 * @param characters 一个个词语
	 * @param pos 词性标注
	 * @return
	 */
    public Sequence[] topKSequences(String[] characters,String[] pos) {
        return this.topKSequences(characters, pos, null);
    }

    /**
     * 最好的K个序列
     * @param characters 一个个词语
     * @param pos 词性标注
     * @param additionaContext
     * @return 
     */
    public Sequence[] topKSequences(String[] characters, String[] pos, Object[] additionaContext) {
        return model.bestSequences(size, characters, pos, additionaContext,
        		contextGenerator, sequenceValidator);
    }
}

