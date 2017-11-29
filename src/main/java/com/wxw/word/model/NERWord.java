package com.wxw.word.model;

/**
 * 基于分词的命名实体标注器
 * @author 王馨苇
 *
 */
public interface NERWord {

	/**
	 * 读入一句分词的语料，得到最终结果
	 * @param sentence 读取的分词的语料
	 * @return
	 */
	public String[] ner(String sentence);
	
	/**
	 * 读入分词的语料，得到命名实体
	 * @param words 词语
	 * @return
	 */
	public String[] ner(String[] words);
}
