package com.wxw.wordandpos.ner;

public interface NERWordAndPosBeamSearchContextGenerator<T> {

	String[] getContext(int index, T[] words, T[] poses, String[] tags, Object[] ac);
}
