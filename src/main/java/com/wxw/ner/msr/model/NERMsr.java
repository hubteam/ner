package com.wxw.ner.msr.model;

/**
 * 命名实体标注器
 * @author 王馨苇
 *
 */
public interface NERMsr {

	/**
	 * 读入一句生语料，进行标注，得到最终结果
	 * @param sentence 读取的生语料
	 * @return
	 */
	public String[] ner(String sentence);
	
	
	
}
