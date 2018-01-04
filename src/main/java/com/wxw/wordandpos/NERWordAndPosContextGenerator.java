package com.wxw.wordandpos;

import opennlp.tools.util.BeamSearchContextGenerator;

public interface NERWordAndPosContextGenerator extends BeamSearchContextGenerator<String>{

	String[] getContext(int index,String[] words, String[] tags, Object[] ac);
	
}
