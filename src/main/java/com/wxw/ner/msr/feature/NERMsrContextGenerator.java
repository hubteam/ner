package com.wxw.ner.msr.feature;

import opennlp.tools.util.BeamSearchContextGenerator;

public interface NERMsrContextGenerator extends BeamSearchContextGenerator<String>{

	public String[] getContext(int index, String[] words, String[] tags, Object[] ac);
}
