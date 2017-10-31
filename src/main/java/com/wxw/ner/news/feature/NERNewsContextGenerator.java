package com.wxw.ner.news.feature;

public interface NERNewsContextGenerator extends NERNewsBeamSearchContextGenerator<String>{

	String[] getContext(int index,String[] words, String[] poses, String[] tags,Object[] ac);
	
}
