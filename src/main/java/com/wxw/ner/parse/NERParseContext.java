package com.wxw.ner.parse;

import com.wxw.ner.sample.NERCharacterSample;
import com.wxw.ner.sample.NERWordAndPosSample;
import com.wxw.ner.sample.NERWordSample;

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
	 * 为基于字的命名实体解析文本
	 * @return
	 */
	public NERCharacterSample parseSample(String sentence){
		return strategy.parse(sentence);
	}
	
	/**
	 * 为基于词性标注的命名实体解析文本
	 * @return
	 */
	public NERWordAndPosSample parseNewsSample(String sentence){
		return strategy.parseNews(sentence);
	}
	
	/**
	 * 为基于分词的命名实体解析文本
	 * @return
	 */
	public NERWordSample parseWordSample(String sentence){
		return strategy.parseWord(sentence);
	}
}
