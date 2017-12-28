package com.wxw.character.ner;

import opennlp.tools.util.BeamSearchContextGenerator;

/**
 * 基于字的命名实体识别特征接口
 * @author 王馨苇
 *
 */
public interface NERCharacterContextGenerator extends BeamSearchContextGenerator<String>{

	/**
	 * 生成上下文特征
	 */
	public String[] getContext(int index, String[] characters, String[] tags, Object[] ac);
}
