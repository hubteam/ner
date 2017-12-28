package com.wxw.word.ner;

import com.wxw.namedentity.NamedEntity;

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
	public NamedEntity[] ner(String sentence);
	
	/**
	 * 读入分词的语料，得到命名实体
	 * @param words 词语
	 * @return
	 */
	public NamedEntity[] ner(String[] words);
	/**
	 * 读入一句分词的语料，得到指定的命名实体
	 * @param sentence 读取的分词的语料
	 * @param flag 命名实体标记
	 * @return
	 */
	public NamedEntity[] ner(String sentence,String flag);
	
	/**
	 * 读入分词的语料，得到指定的命名实体
	 * @param words 词语
	 * @param flag 命名实体标记
	 * @return
	 */
	public NamedEntity[] ner(String[] words,String flag);
	
	/**
	 * 读入一句分词的语料，得到最好的K个结果
	 * @param sentence 读取的分词的语料
	 * @return
	 */
	public NamedEntity[][] ner(int k,String sentence);
	
	/**
	 * 读入分词的语料，得到最好的K个命名实体
	 * @param words 词语
	 * @return
	 */
	public NamedEntity[][] ner(int k,String[] words);
}
