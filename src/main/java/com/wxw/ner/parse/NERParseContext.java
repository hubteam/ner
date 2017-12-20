package com.wxw.ner.parse;

import com.wxw.ner.sample.NERWordOrCharacterSample;

/**
 * 上下文类
 * @author 王馨苇
 *
 */
public class NERParseContext {

	private NERParseStrategy strategy;
	
	public NERParseContext(NERParseStrategy strategy){
		this.strategy = strategy;
	}

	/**
	 * 命名实体解析文本
	 * @return
	 */
	public NERWordOrCharacterSample parseSample(String sentence){
		return strategy.parse(sentence);
	}
}
