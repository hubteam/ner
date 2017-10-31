package com.wxw.ner.msr.model;

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

import com.wxw.ner.event.NERMsrSampleEvent;
import com.wxw.ner.msr.feature.NERMsrContextGenerator;
import com.wxw.ner.sample.FileInputStreamFactory;
import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERMsrSampleStream;
import com.wxw.ner.sequence.DefaultNERMsrSequenceValidator;

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
 * 训练模型
 * @author 王馨苇
 *
 */
public class NERMsrME implements NERMsr{

	public static final int DEFAULT_BEAM_SIZE = 3;
	private NERMsrContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private SequenceClassificationModel<String> model;
	private NERMsrModel modelPackage;
	private List<String> characters = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

    private SequenceValidator<String> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public NERMsrME(NERMsrModel model, NERMsrContextGenerator contextGen) {
		init(model , contextGen);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	private void init(NERMsrModel model, NERMsrContextGenerator contextGen) {
		int beamSize = NERMsrME.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

        modelPackage = model;

        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultNERMsrSequenceValidator();
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
	public static NERMsrModel train(File file, TrainingParameters params, NERMsrContextGenerator contextGen,
			String encoding){
		NERMsrModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERMsrSample> sampleStream = new NERMsrSampleStream(lineStream);
			model = NERMsrME.train("zh", sampleStream, params, contextGen);
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
	public static NERMsrModel train(String languageCode, ObjectStream<NERMsrSample> sampleStream, TrainingParameters params,
			NERMsrContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = NERMsrME.DEFAULT_BEAM_SIZE;
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
            ObjectStream<Event> es = new NERMsrSampleEvent(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
            posModel = trainer.train(es);                       
        }

        if (posModel != null) {
            return new NERMsrModel(languageCode, posModel, beamSize, manifestInfoEntries);
        } else {
            return new NERMsrModel(languageCode, seqPosModel, manifestInfoEntries);
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
	public static NERMsrModel train(File file, File modelbinaryFile, File modeltxtFile, TrainingParameters params,
			NERMsrContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		PlainTextGISModelWriter modelWriter = null;
		NERMsrModel model = null;
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<NERMsrSample> sampleStream = new NERMsrSampleStream(lineStream);
			model = NERMsrME.train("zh", sampleStream, params, contextGen);
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
	public static NERMsrModel readModel(File modelFile, TrainingParameters params, NERMsrContextGenerator contextGen,
			String encoding) {
		PlainTextGISModelReader modelReader = null;
		AbstractModel abModel = null;
		NERMsrModel model = null;
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
	      
        int beamSize = NERMsrME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

		try {
			Map<String, String> manifestInfoEntries = new HashMap<String, String>();
			modelReader = new PlainTextGISModelReader(modelFile);			
			abModel = modelReader.getModel();
			model =  new NERMsrModel(encoding, abModel, beamSize,manifestInfoEntries);
	
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
	public String[] ner(String sentence) {
		String[] tags = tag(sentence);
		String word = new String();
        ArrayList<String> words = new ArrayList<String>();
        for (int i = 0; i < tags.length; i++) {
            word += sentence.charAt(i);

            if (tags[i].equals("S") || tags[i].equals("E")) {
                words.add(word);
                word = "";
            }
        }

        if (word.length() > 0) {
            words.add(word);
        }

        return words.toArray(new String[words.size()]);
	}
	public String[] ner(String sentence, String flag) {
		String[] tags = tag(sentence);
		return null;
	}
	
	/**
	 * 得到标注的结果
	 * @param sentence 读入的语料转成的字符串数组
	 * @return
	 */
	public String[] tag(String[] sentence) {
        return this.tag(sentence, null);
    }

	/**
	 * 生语料转换成字符串数组
	 * @param sentence 读入的生语料
	 * @return
	 */
    public String[] tag(String sentence) {
        String[] chars = new String[sentence.length()];

        for (int i = 0; i < sentence.length(); i++) {
            chars[i] = sentence.charAt(i) + "";
        }

        return tag(chars);
    }
}

