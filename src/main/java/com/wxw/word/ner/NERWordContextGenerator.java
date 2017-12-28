package com.wxw.word.ner;

import opennlp.tools.util.BeamSearchContextGenerator;

/**
 * 基于分词的命名实体识别特征接口
 * @author 王馨苇
 *
 */
public interface NERWordContextGenerator extends BeamSearchContextGenerator<String>{

	/**
	 * 生成上下文特征
	 * @param index 索引
	 * @param words 词语
	 * @param tags 命名实体标记
	 * @param ac
	 * @return
	 */
	public String[] getContext(int index, String[] words, String[] tags, Object[] ac);
}
