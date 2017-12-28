package com.wxw.character;

import com.wxw.namedentity.NamedEntity;

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
	public NamedEntity[] ner(String sentence);
	
	/**
	 * 读入一段单个字组成的语料
	 * @param sentence 单个字组成的数组
	 * @return
	 */
	public NamedEntity[] ner(String[] sentence);
	/**
	 * 读入一句生语料，进行标注，得到指定的命名实体
	 * @param sentence 读取的生语料
	 * @param flag 命名实体标记
	 * @return
	 */
	public NamedEntity[] ner(String sentence,String flag);
	
	/**
	 * 读入一段单个字组成的语料,得到指定的命名实体
	 * @param sentence 单个字组成的数组
	 * @param flag 命名实体标记
	 * @return
	 */
	public NamedEntity[] ner(String[] sentence,String flag);
	/**
	 * 读入一句生语料，进行标注，得到最好的K个结果
	 * @param sentence 读取的生语料
	 * @return
	 */
	public NamedEntity[][] ner(int k,String sentence);
	
	/**
	 * 读入一段单个字组成的语料,得到最好的K个结果
	 * @param sentence 单个字组成的数组
	 * @return
	 */
	public NamedEntity[][] ner(int k,String[] sentence);
}
