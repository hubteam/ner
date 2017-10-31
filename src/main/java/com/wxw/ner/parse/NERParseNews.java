package com.wxw.ner.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class NERParseNews implements NERParseStrategy{

	private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> tags = new ArrayList<String>();
    private ArrayList<String> poses = new ArrayList<String>();
    
    /**
     * 解析语料，基于字的
     */
	public NERNewsSample parseNews(String sentence) {
		String[] str = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
		Queue<String> queue = new LinkedList<String>();
		int i = 1;
		String word = "";
		String pos = "";
		String tag = "";
		String ner = "";
		while(i < str.length) {			
			word = getWordAndPos(str,i)[0];
			pos = getWordAndPos(str,i)[1];
			if(word.startsWith("[")){
				word = word.substring(1);
				while(!pos.contains("]")){
					queue.offer(word);
					poses.add(pos);
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];					
				}
				int index = pos.indexOf("]");
				tag = pos.substring(index+1);
				queue.offer(word);
				poses.add(pos.substring(0, index));
				if(tag.equals("nr")){
					ner = "nr";
					add(queue,ner);
				}else if(tag.equals("ns")){
					ner = "ns";
					add(queue,ner);
				}else if(tag.equals("nt")){
					ner = "nt";
					add(queue,ner);
				}else if(tag.equals("nz")){
					ner = "nz";
					add(queue,ner);
				}
			}else if(pos.equals("nr")){
				ner = "nr";
				while(pos.equals("nr")){
					queue.offer(word);
					poses.add(pos);			
					
					if(i+1 < str.length){
						i++;
//						word = getWordAndPos(str,i)[0];
//						pos = getWordAndPos(str,i)[1];
//						if(pos.equals("n") && word.equals("主席")){							
//							queue.offer(word);
//							poses.add(pos);
//						}
					}else{
						break;
					}
					if(queue.size() == 2){
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(queue,ner);
				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("ns")){
				ner = "ns";
				while(pos.equals("ns")){
					queue.offer(word);
					poses.add(pos);
					if((i+1) < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(queue,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("nt")){
				ner = "nt";
				while(pos.equals("nt")){
					queue.offer(word);
					poses.add(pos);
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(queue,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("nz")){
				ner = "nz";
				while(pos.equals("nz")){
					queue.offer(word);
					poses.add(pos);
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(queue,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else{
				words.add(word);
				tags.add("o");
				poses.add(pos);
			}
			
			if(i+1 < str.length){
				i++;
			}else{
				break;
			}
		}
//		for (int j = 0; j < words.size(); j++) {
//			System.out.println(words.get(j)+"--"+tags.get(j)+"--"+poses.get(j));
//		}
		return new NERNewsSample(words,poses,tags);
	}
	
	public String[] getWordAndPos(String[] str, int i){
		String[] temp = str[i].split("/");
		return temp;
	}
	
	public void add(Queue<String> queue,String tag){
		if(queue.size() == 1){
			words.add(queue.poll());
			tags.add("s_"+tag);
		}else if(queue.size() == 2){
			words.add(queue.poll());
			tags.add("b_"+tag);
			words.add(queue.poll());
			tags.add("e_"+tag);
		}else if(queue.size() > 2){
			String temp;
			int count = 1;
			int size = queue.size();
			while((temp = queue.poll()) != null){
				if(count == 1){
					words.add(temp);
					tags.add("b_"+tag);
					count++;
				}else if(count == size){
					words.add(temp);
					tags.add("e_"+tag);
					count++;
				}else{
					words.add(temp);
					tags.add("m_"+tag);
					count++;
				}	
			}
		}
		queue.clear();
	}

	public NERMsrSample parse(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}


}