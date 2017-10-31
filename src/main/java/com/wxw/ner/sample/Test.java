package com.wxw.ner.sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Test {

	static ArrayList<String> words = new ArrayList<String>();
	static ArrayList<String> tags = new ArrayList<String>();
	static ArrayList<String> poses = new ArrayList<String>();
	
	public static void main(String[] args) {
		String sentence = "致公党中央/nt 领导人多次参加/o 中共中央/nt 和/o 国务院/nt 举行的民主党派人士座谈会、协商会，参与国家大政方针的协商，认真履行参政议政、民主监督职能；/o ";
		String[] wordsAndPoses = sentence.split(" ");
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
//		Queue<String> queue = new LinkedList<String>();
//		
//		int i = 0;
//		String word = "";
//		String pos = "";
//		String tag = "";
//		String ner = "";
//		while(i < str.length) {			
//			word = getWordAndPos(str,i)[0];
//			pos = getWordAndPos(str,i)[1];
//			if(word.startsWith("[")){
//				word = word.substring(1);
//				while(!pos.contains("]")){
//					queue.offer(word);
//					poses.add(pos);
//					if(i+1 < str.length){
//						i++;
//					}else{
//						break;
//					}
//					
//					word = getWordAndPos(str,i)[0];
//					pos = getWordAndPos(str,i)[1];					
//				}
//				int index = pos.indexOf("]");
//				tag = pos.substring(index+1);
//				queue.offer(word);
//				poses.add(pos.substring(0, index));
//				if(tag.equals("nr")){
//					ner = "PER";
//					add(queue,ner);
//				}else if(tag.equals("ns")){
//					ner = "LOC";
//					add(queue,ner);
//				}else if(tag.equals("nt")){
//					ner = "ORG";
//					add(queue,ner);
//				}else if(tag.equals("nz")){
//					ner = "PNS";
//					add(queue,ner);
//				}
//			}else if(pos.equals("nr")){
//				ner = "PER";
//				while(pos.equals("nr")){
//					queue.offer(word);
//					poses.add(pos);
//					if(queue.size() == 2){
//						break;
//					}
//					if(i+1 < str.length){
//						i++;
//					}else{
//						break;
//					}
//					word = getWordAndPos(str,i)[0];
//					pos = getWordAndPos(str,i)[1];	
//				}
//				add(queue,ner);
//			}else if(pos.equals("ns")){
//				ner = "LOC";
//				while(pos.equals("ns")){
//					queue.offer(word);
//					poses.add(pos);
//					if(i+1 < str.length){
//						i++;
//					}else{
//						break;
//					}
//					word = getWordAndPos(str,i)[0];
//					pos = getWordAndPos(str,i)[1];	
//				}
//				add(queue,ner);
//			}else if(pos.equals("nt")){
//				ner = "ORG";
//				while(pos.equals("nt")){
//					queue.offer(word);
//					poses.add(pos);
//					if(i+1 < str.length){
//						i++;
//					}else{
//						break;
//					}
//					word = getWordAndPos(str,i)[0];
//					pos = getWordAndPos(str,i)[1];	
//				}
//				add(queue,ner);
//			}else if(pos.equals("nz")){
//				ner = "PNS";
//				while(pos.equals("nz")){
//					queue.offer(word);
//					poses.add(pos);
//					if(i+1 < str.length){
//						i++;
//					}else{
//						break;
//					}
//					word = getWordAndPos(str,i)[0];
//					pos = getWordAndPos(str,i)[1];	
//				}
//				add(queue,ner);
//			}else{
//				words.add(word);
//				tags.add("OUT");
//				poses.add(pos);
//			}
//			
//			if(i+1 < str.length){
//				i++;
//			}else{
//				break;
//			}
//		}
		for (int j = 0; j < words.size(); j++) {
			System.out.println(words.get(j)+"--"+tags.get(j));
		}
	}
	
	public static String[] getWordAndPos(String[] str, int i){
		String[] temp = str[i].split("/");
		return temp;
	}
	
	public static void add(Queue<String> queue,String tag){
		if(queue.size() == 1){
			words.add(queue.poll());
			tags.add("S"+tag);
		}else if(queue.size() == 2){
			words.add(queue.poll());
			tags.add("B"+tag);
			words.add(queue.poll());
			tags.add("E"+tag);
		}else if(queue.size() > 2){
			String temp;
			int count = 1;
			int size = queue.size();
			while((temp = queue.poll()) != null){
				if(count == 1){
					words.add(temp);
					tags.add("B"+tag);
					count++;
				}else if(count == size){
					words.add(temp);
					tags.add("E"+tag);
					count++;
				}else{
					words.add(temp);
					tags.add("I"+tag);
					count++;
				}	
			}
		}
		queue.clear();
	}
}
