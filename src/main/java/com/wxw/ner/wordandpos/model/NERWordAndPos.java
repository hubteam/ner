package com.wxw.ner.wordandpos.model;

/**
 * 基于词性标注的命名实体识别
 * @author 王馨苇
 *
 */
public interface NERWordAndPos {

	/**
	 * 读入一句词性标注的语料，得到最终结果
	 * @param sentence 读取的词性标注的语料
	 * @return
	 */
	public String[] ner(String sentence);
	
	/**
	 * 读入词性标注的语料，得到命名实体
	 * @param words 词语
	 * @param poses 词性
	 * @return
	 */
	public String[] ner(String[] words,String[] poses);
}
