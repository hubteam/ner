package com.wxw.ner.character.model;

/**
 * 基于字的,一次只训练一种命名实体的命名实体标注器
 * @author 王馨苇
 *
 */
public interface NERCharacterSingle {

	/**
	 * 读入一句生语料，进行标注，得到最终结果
	 * @param sentence 读取的生语料
	 * @param nerflag 命名实体类型
	 * @return
	 */
	public String[] ner(String sentence, String nerflag);
	
	/**
	 * 读入一段单个字组成的语料
	 * @param sentence 单个字组成的数组
	 * @param nerflag 命名实体类型
	 * @return
	 */
	public String[] ner(String[] sentence, String nerflag);
}
