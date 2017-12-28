package com.wxw.wordandpos;

public interface NERWordAndPosBeamSearchContextGenerator<T> {

	String[] getContext(int index, T[] words, T[] poses, String[] tags, Object[] ac);
}
