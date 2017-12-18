package com.wxw.word.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.wxw.ner.character.model.NERCharacterME;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.util.model.BaseModel;

/**
 * 基于分词的命名实体识别模型
 * @author 王馨苇
 *
 */
public class NERWordModel extends BaseModel{
	private static final String COMPONENT_NAME = "NERME";
	private static final String NER_MODEL_ENTRY_NAME = "NER.model";
	
	/**
	 * 构造
	 * @param componentName 训练模型的类
	 * @param modelFile 模型文件
	 * @throws IOException IO异常
	 */
	protected NERWordModel(String componentName, File modelFile) throws IOException {
		super(COMPONENT_NAME, modelFile);
	}

	public NERWordModel(InputStream modelFile) throws IOException {
		super(COMPONENT_NAME, modelFile);
	}
	
	/**
	 * 构造
	 * @param languageCode 编码
	 * @param posModel 最大熵模型
	 * @param beamSize 大小
	 * @param manifestInfoEntries 配置的信息
	 */
	public NERWordModel(String languageCode, MaxentModel posModel, int beamSize,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (posModel == null) {
            throw new IllegalArgumentException("The maxentPosModel param must not be null!");
        }

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        manifest.setProperty(BeamSearch.BEAM_SIZE_PARAMETER, Integer.toString(beamSize));

        //放入新训练出来的模型
        artifactMap.put(NER_MODEL_ENTRY_NAME, posModel);
        checkArtifactMap();
	}
	

	public NERWordModel(String languageCode, SequenceClassificationModel<String> seqModel,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (seqModel == null) {
            throw new IllegalArgumentException("The maxent wordsegModel param must not be null!");
        }

        artifactMap.put(NER_MODEL_ENTRY_NAME, seqModel);		
	}

	/**
	 * 获取模型
	 * @return 最大熵模型
	 */
	public MaxentModel getNERModel() {
		if (artifactMap.get(NER_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            return (MaxentModel) artifactMap.get(NER_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
	}
	
	public SequenceClassificationModel<String> getNERSequenceModel() {

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);

        if (artifactMap.get(NER_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            String beamSizeString = manifest.getProperty(BeamSearch.BEAM_SIZE_PARAMETER);

            int beamSize = NERCharacterME.DEFAULT_BEAM_SIZE;
            if (beamSizeString != null) {
                beamSize = Integer.parseInt(beamSizeString);
            }

            return new BeamSearch<String>(beamSize, (MaxentModel) artifactMap.get(NER_MODEL_ENTRY_NAME));
        } else if (artifactMap.get(NER_MODEL_ENTRY_NAME) instanceof SequenceClassificationModel) {
            return (SequenceClassificationModel) artifactMap.get(NER_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
    }
}
