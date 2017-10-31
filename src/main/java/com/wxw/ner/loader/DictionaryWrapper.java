package com.wxw.ner.loader;

import java.util.Set;

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
