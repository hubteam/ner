package com.wxw.ner.dict;

import java.util.Set;

/**
 * 词典和词典中最长的词的长度
 * @author 王馨苇
 *
 */
public class DictionaryWrapper {

	private int length;
	private Set<String> wordSet;
	
	public DictionaryWrapper(int length,Set<String> wordSet){
		this.length = length;
		this.wordSet = wordSet;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public Set<String> getWordSet(){
		return this.wordSet;
	}
}
