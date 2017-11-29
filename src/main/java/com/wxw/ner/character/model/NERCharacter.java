package com.wxw.ner.character.model;

/**
 * 基于字的命名实体标注器
 * @author 王馨苇
 *
 */
public interface NERCharacter {

	/**
	 * 读入一句生语料，进行标注，得到最终结果
	 * @param sentence 读取的生语料
	 * @return
	 */
	public String[] ner(String sentence);
	
	/**
	 * 读入一段单个字组成的语料
	 * @param sentence 单个字组成的数组
	 * @return
	 */
	public String[] ner(String[] sentence);
	
}
