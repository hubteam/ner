package com.wxw.wordandpos.ner;

public interface NERWordAndPosContextGenerator extends NERWordAndPosBeamSearchContextGenerator<String>{

	String[] getContext(int index,String[] words, String[] poses, String[] tags,Object[] ac);
	
}
