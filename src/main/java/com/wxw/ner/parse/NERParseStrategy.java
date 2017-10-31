package com.wxw.ner.parse;

import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

public interface NERParseStrategy {

	public NERMsrSample parse(String sentence);
	public NERNewsSample parseNews(String sentence);
}
