package com.wxw.ner.news.feature;

public interface NERNewsBeamSearchContextGenerator<T> {

	String[] getContext(int index, T[] words, T[] poses, String[] tags, Object[] ac);
}
