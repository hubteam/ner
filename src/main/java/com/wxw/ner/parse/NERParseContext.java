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

	private String sentence;
	private NERParseStrategy strategy;
	
	public NERParseContext(NERParseStrategy strategy, String sentence){
		this.sentence = sentence;
		this.strategy = strategy;
	}

	/**
	 * 为基于字的命名实体解析文本
	 * @return
	 */
	public NERCharacterSample parseSample(){
		return strategy.parse(sentence);
	}
	
	/**
	 * 为基于词性标注的命名实体解析文本
	 * @return
	 */
	public NERWordAndPosSample parseNewsSample(){
		return strategy.parseNews(sentence);
	}
	
	/**
	 * 为基于分词的命名实体解析文本
	 * @return
	 */
	public NERWordSample parseWordSample(){
		return strategy.parseWord(sentence);
	}
}
