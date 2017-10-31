package com.wxw.ner.parse;

import java.util.ArrayList;

import com.wxw.ner.run.NERMsrRunSingle;
import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class NERParseMSRSingle implements NERParseStrategy{

	public NERMsrSample parse(String sentence) {
		//改进变成单个字的
		String type = NERMsrRunSingle.type;
//		System.out.println(type);
				String[] wordsAndPoses = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
				
				ArrayList<String> words = new ArrayList<String>();
			    ArrayList<String> tags = new ArrayList<String>();
			    
			    for (int i = 0; i < wordsAndPoses.length; i++) {
//			    	String[] wordanspos = wordsAndPoses[i].split("/");
//			    	String word = wordanspos[0];
//			    	String tag = wordanspos[1];
//			    	words.add(word);
//			    	tags.add(tag);
			    	
			    	//改进变成单个字的
			    	String[] wordanspos = wordsAndPoses[i].split("/");
			    	String word = wordanspos[0];
			    	String tag = wordanspos[1];
			    	if(!tag.equals(type)){
			    		for (int j = 0; j < word.length(); j++) {
							words.add(word.charAt(j)+"");
							tags.add("o");
						}
			    	}else if(tag.equals(type)){
			    		if(word.length() == 1){
			    			words.add(word);
			    			tags.add("s_"+type);
			    		}else if(word.length() == 2){
			    			words.add(word.charAt(0)+"");
			    			tags.add("b_"+type);
			    			words.add(word.charAt(1)+"");
			    			tags.add("e_"+type);
			    		}else if(word.length() > 2){
			    			for (int j = 0; j < word.length(); j++) {
								if(j == 0){
									words.add(word.charAt(0)+"");
									tags.add("b_"+type);
								}else if(j == word.length()-1){
									words.add(word.charAt(j)+"");
									tags.add("e_"+type);
								}else{
									words.add(word.charAt(j)+"");
									tags.add("m_"+type);
								}
							}
			    		}
			    	}	
				}
				return new NERMsrSample(words,tags);
	}

	public NERNewsSample parseNews(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

}
