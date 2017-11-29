package com.wxw.ner.wordandpos.feature;

public interface NERWordAndPosBeamSearchContextGenerator<T> {

	String[] getContext(int index, T[] words, T[] poses, String[] tags, Object[] ac);
}
