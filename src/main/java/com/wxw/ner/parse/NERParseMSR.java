package com.wxw.ner.parse;

import java.util.ArrayList;

import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class NERParseMSR implements NERParseStrategy{

	public NERMsrSample parse(String sentence) {

		//改进变成单个字的
		String[] wordsAndPoses = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
		
		ArrayList<String> words = new ArrayList<String>();
	    ArrayList<String> tags = new ArrayList<String>();
	    
	    for (int i = 0; i < wordsAndPoses.length; i++) {
//	    	String[] wordanspos = wordsAndPoses[i].split("/");
//	    	String word = wordanspos[0];
//	    	String tag = wordanspos[1];
//	    	words.add(word);
//	    	tags.add(tag);
	    	
	    	//改进变成单个字的
	    	String[] wordanspos = wordsAndPoses[i].split("/");
	    	String word = wordanspos[0];
	    	String tag = wordanspos[1];
	    	if(tag.equals("o")){
	    		for (int j = 0; j < word.length(); j++) {
					words.add(word.charAt(j)+"");
					tags.add("o");
				}
	    	}else if(tag.equals("nr")){
	    		if(word.length() == 1){
	    			words.add(word);
	    			tags.add("s_nr");
	    		}else if(word.length() == 2){
	    			words.add(word.charAt(0)+"");
	    			tags.add("b_nr");
	    			words.add(word.charAt(1)+"");
	    			tags.add("e_nr");
	    		}else if(word.length() > 2){
	    			for (int j = 0; j < word.length(); j++) {
						if(j == 0){
							words.add(word.charAt(0)+"");
							tags.add("b_nr");
						}else if(j == word.length()-1){
							words.add(word.charAt(j)+"");
							tags.add("e_nr");
						}else{
							words.add(word.charAt(j)+"");
							tags.add("m_nr");
						}
					}
	    		}
	    	}else if(tag.equals("nt")){
	    		if(word.length() == 1){
	    			words.add(word);
	    			tags.add("s_nt");
	    		}else if(word.length() == 2){
	    			words.add(word.charAt(0)+"");
	    			tags.add("b_nt");
	    			words.add(word.charAt(1)+"");
	    			tags.add("e_nt");
	    		}else if(word.length() > 2){
	    			for (int j = 0; j < word.length(); j++) {
						if(j == 0){
							words.add(word.charAt(0)+"");
							tags.add("b_nt");
						}else if(j == word.length()-1){
							words.add(word.charAt(j)+"");
							tags.add("e_nt");
						}else{
							words.add(word.charAt(j)+"");
							tags.add("m_nt");
						}
					}
	    		}
	    	}else if(tag.equals("ns")){
	    		if(word.length() == 1){
	    			words.add(word);
	    			tags.add("s_ns");
	    		}else if(word.length() == 2){
	    			words.add(word.charAt(0)+"");
	    			tags.add("b_ns");
	    			words.add(word.charAt(1)+"");
	    			tags.add("e_ns");
	    		}else if(word.length() > 2){
	    			for (int j = 0; j < word.length(); j++) {
						if(j == 0){
							words.add(word.charAt(0)+"");
							tags.add("b_ns");
						}else if(j == word.length()-1){
							words.add(word.charAt(j)+"");
							tags.add("e_ns");
						}else{
							words.add(word.charAt(j)+"");
							tags.add("m_ns");
						}
					}
	    		}
	    	}	
		}
		return new NERMsrSample(words,tags);
	}

	public NERNewsSample parseNews(String sentence) {
	
		return null;
	}

}
