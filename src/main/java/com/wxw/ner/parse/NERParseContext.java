package com.wxw.ner.parse;

import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

public class NERParseContext {

	private String sentence;
	private NERParseStrategy strategy;
	private NERParseNews parse;
	
	public NERParseContext(NERParseStrategy strategy, String sentence){
		this.sentence = sentence;
		this.strategy = strategy;
	}
	
	public NERMsrSample parseSample(){
		return strategy.parse(sentence);
	}
	
	public NERNewsSample parseNewsSample(){
		return strategy.parseNews(sentence);
	}
}
