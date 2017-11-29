package com.wxw.word.model;

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

import com.wxw.ner.event.NERWordSampleEvent;
import com.wxw.ner.sample.FileInputStreamFactory;
import com.wxw.ner.sample.NERWordSample;
import com.wxw.ner.sample.NERWordSampleStream;
import com.wxw.ner.sequence.DefaultNERWordSequenceValidator;
import com.wxw.word.feature.NERWordContextGenerator;

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
 * 为基于分词的命名实体识别训练模型
 * @author 王馨苇
 *
 */
public class NERWordME implements NERWord{

	public static final int DEFAULT_BEAM_SIZE = 3;
	private NERWordContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private SequenceClassificationModel<String> model;
	private NERWordModel modelPackage;
	private List<String> characters = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

    private SequenceValidator<String> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public NERWordME(NERWordModel model, NERWordContextGenerator contextGen) {
		init(model , contextGen);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	private void init(NERWordModel model, NERWordContextGenerator contextGen) {
		int beamSize = NERWordME.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

        modelPackage = model;

        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultNERWordSequenceValidator();
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
	public static NERWordModel train(File file, TrainingParameters params, NERWordContextGenerator contextGen,
			String encoding){
		NERWordModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERWordSample> sampleStream = new NERWordSampleStream(lineStream);
			model = NERWordME.train("zh", sampleStream, params, contextGen);
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
	public static NERWordModel train(String languageCode, ObjectStream<NERWordSample> sampleStream, TrainingParameters params,
			NERWordContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = NERWordME.DEFAULT_BEAM_SIZE;
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
            ObjectStream<Event> es = new NERWordSampleEvent(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
            posModel = trainer.train(es);                       
        }

        if (posModel != null) {
            return new NERWordModel(languageCode, posModel, beamSize, manifestInfoEntries);
        } else {
            return new NERWordModel(languageCode, seqPosModel, manifestInfoEntries);
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
	public static NERWordModel train(File file, File modelbinaryFile, File modeltxtFile, TrainingParameters params,
			NERWordContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		PlainTextGISModelWriter modelWriter = null;
		NERWordModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERWordSample> sampleStream = new NERWordSampleStream(lineStream);
			model = NERWordME.train("zh", sampleStream, params, contextGen);
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
	 * 得到最好的一个序列
	 * @param characters
	 * @param additionaContext
	 * @return
	 */
	public String[] tag(String[] characters,Object[] additionaContext){
		bestSequence = model.bestSequence(characters, additionaContext, contextGenerator,sequenceValidator);
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
	public static NERWordModel readModel(File modelFile, TrainingParameters params, NERWordContextGenerator contextGen,
			String encoding) {
		PlainTextGISModelReader modelReader = null;
		AbstractModel abModel = null;
		NERWordModel model = null;
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
	      
        int beamSize = NERWordME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

		try {
			Map<String, String> manifestInfoEntries = new HashMap<String, String>();
			modelReader = new PlainTextGISModelReader(modelFile);			
			abModel = modelReader.getModel();
			model =  new NERWordModel(encoding, abModel, beamSize,manifestInfoEntries);
	
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
     * 最好的K个序列
     * @param words 一个个词语
     * @param additionaContext
     * @return 
     */
    public Sequence[] topKSequences(String[] words, Object[] additionaContext) {
        return model.bestSequences(size, words, additionaContext,
        		contextGenerator, sequenceValidator);
    }
	
    /**
	 * 最好的K个序列
	 * @param words 一个个词语
	 * @return
	 */
    public Sequence[] topKSequences(String[] words) {
        return this.topKSequences(words, null);
    }
    
    /**
	 * 得到最好的numTaggings个标记序列
	 * @param numTaggings 个数
	 * @param characters 一个个词语
	 * @return 分词加词性标注的序列
	 */
	public String[][] tag(int numTaggings, String[] words) {
        Sequence[] bestSequences = model.bestSequences(numTaggings, words, null,
        		contextGenerator, sequenceValidator);
        String[][] tagsandposes = new String[bestSequences.length][];
        for (int si = 0; si < tagsandposes.length; si++) {
            List<String> t = bestSequences[si].getOutcomes();
            tagsandposes[si] = t.toArray(new String[t.size()]);
        }
        return tagsandposes;
    }
    
	/**
	 * 对分词之后的句子进行命名实体识别,分词之间用空格隔开
	 */
	@Override
	public String[] ner(String sentence) {
		String[] words = sentence.split(" ");
		return ner(words);
	}

	/**
	 * 对分词之后的数组进行命名实体识别
	 */
	@Override
	public String[] ner(String[] words) {
		String[] tags = tag(words,null);
		String[] nerwords = NERWordSample.toWord(words, tags);
		String[] nertags = NERWordSample.toPos(tags);
		String[] output = null;
		for (int i = 0; i < nerwords.length; i++) {
			output[i] = nerwords[i]+"/"+nertags[i];
		}
 		return output;
	}
}
