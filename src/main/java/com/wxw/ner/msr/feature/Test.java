package com.wxw.ner.msr.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("wxw");
		System.out.println(set.contains("xw"));
//		int selected = 0;
//		int target = 0;
//		int selectednr = 0;
//		int targetnr = 0;
//		int selectednt = 0;
//		int targetnt = 0;
//		int selectedns = 0;
//		int targetns = 0;
//		int truePositive = 0;
//		int truePositivenr = 0;
//		int truePositivens = 0;
//		int truePositivent = 0;
//		String[] wordsRef = {"希腊","人将","瓦西里斯","与","奥纳西斯","比较时总不忘补充一句：他和","奥纳西斯","不同，他没有改组家庭"};
//		String[] tagsRef = {"ns","o","nr","o","nr","o","nr","o"};
//		String[] wordsPre = {"希腊","人将","瓦西里","斯与","奥纳西","斯比较时总不忘补充一句：他和","奥纳西","斯不同，他没有改组家庭"};
//		String[] tagsPre = {"ns","o", "nr", "o", "nr" ,"o" ,"nr", "o" };
//		//定义变量记录当前扫描的总长度
//    	int countRef = 0,countPre = 0;
//    	//记录当前所在的词的位置
//    	int i = 0,j = 0;
//    	//记录i的前一次的值
//    	int iPre = -1;
//    	if(wordsRef.length > 0 && wordsPre.length > 0){
//    		while(wordsRef[i] != null || !("".equals(wordsRef[i]))|| wordsPre[j] != null || !("".equals(wordsPre[j]))){
//        		countRef += wordsRef[i].length();
//        		countPre += wordsPre[j].length();
//        		
//        		//匹配的情况
//        		if((wordsRef[i] == wordsPre[j] || wordsRef[i].equals(wordsPre[j]))){
//
//        			if(!tagsRef[i].equals("o") && !tagsPre[j].equals("o") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
//        					
//        				truePositive++;        				
//        			}       			
//        			if(tagsRef[i].equals("nr") && tagsPre[j].equals("nr") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
//    					
//        				truePositivenr++;        				
//        			} 
// 
//        			if(tagsRef[i].equals("ns") && tagsPre[j].equals("ns") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
//    					
//        				truePositivens++;  
//        			}
//
//        			if(tagsRef[i].equals("nt") && tagsPre[j].equals("nt") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
//    					
//        				truePositivent++;  
//        			}
//
//        			iPre = i;
//    				//两个字符串同时向后扫描
//        			i++;j++;
//        			//为了防止：已经到达边界了，还用references[i]或者predictions[i]来判断，此时越界了
//    				if(i >= wordsRef.length || j >= wordsPre.length)
//    					break;
//   
//        		}else{
//        			//不匹配的情况，则需要比较当前扫过的总长度
//        			//（1）：长度长的那个不动，长度短的那个要继续向前扫描比较   			
//        			if(countRef > countPre){
//    					iPre = i;
//        				j++;
//    					countRef -= wordsRef[i].length();
//    					if(j >= wordsPre.length)
//    						break;
//    					//（2）：长度相等的时候，二者都需要向前扫描
//    				}else if(countRef == countPre){
//    					iPre = i;
//    					i++;j++;
//    					if(i >= wordsRef.length || j >= wordsPre.length)
//    						break;
//    					//（1）：长度长的那个不动，长度短的那个要继续向前扫描比较
//    				}else if(countRef < countPre){
//    					iPre = i;
//    					i++;
//    					countPre -= wordsPre[j].length();
//    					if(i >= wordsRef.length)
//    						break;
//    				}
//        		}
//        	}
//    	}
//    	for (int k = 0; k < tagsPre.length; k++) {
//    		if(!tagsPre[k].equals("o")){
//    			selected++;
//    		}
//    		
//    		if(tagsPre[k].equals("nr")){
//    			selectednr++;
//    		}
//    		if(tagsPre[k].equals("ns")){
//    			selectedns++;
//    		}
//    		if(tagsPre[k].equals("nt")){
//    			selectednt++;
//    		}
//    		
//		}
//    	for (int k = 0; k < tagsRef.length; k++) {
//    		if(tagsRef[k].equals("nr")){
//    			targetnr++;
//    		}
//    		if(tagsRef[k].equals("ns")){
//    			targetns++;
//    		}
//    		if(tagsRef[k].equals("nt")){
//    			targetnt++;
//    		}
//    		if(!tagsRef[k].equals("o")){
//    			target++;
//    		}
//		}
//    	System.out.println("selected:"+selected);
//    	System.out.println("target:"+target);
//    	System.out.println("truePositive:"+truePositive);
//    	System.out.println("selectednr:"+selectednr);
//    	System.out.println("targetnr:"+targetnr);
//    	System.out.println("truePositivenr:"+truePositivenr);
//    	System.out.println("selectedns:"+selectedns);
//    	System.out.println("targetns:"+targetns);
//    	System.out.println("truePositivens:"+truePositivens);
//    	System.out.println("selectednt:"+selectednt);
//    	System.out.println("targetnt:"+targetnt);
//    	System.out.println("truePositivent:"+truePositivent);
		
		String[] str= {"null","1","2","3","4","5","6","7","null"};
		String s = "";
		s+="null";
		s+="123";
		System.out.println(s.contains("null"));
		find(4,str);
//		String str = "o o o o o o o o o o o o o o o o b_nr m_nr";
//		String str2 = "全 国 人 民 代 表 大 会 常 务 委 员 会 秘 书 长 曹 志";
//		String[] tagsandposesPre = str.split(" ");
//		String[] characters = str2.split(" ");
//		List<String> poses = new ArrayList<String>();
//		String word = new String();
//        ArrayList<String> words = new ArrayList<String>();       
//		int i = 0;
//		while(i < tagsandposesPre.length){
//			if(tagsandposesPre[i].equals("o")){				
//				while(tagsandposesPre[i].equals("o")){
//					word += characters[i];
//					i++;
//					if(i == tagsandposesPre.length){
//						break;
//					}
//				}
//				words.add(word);
//				word = "";
//			}else{
//				word += characters[i];
//				String tag = tagsandposesPre[i].split("_")[0];
//				String pos = tagsandposesPre[i].split("_")[1];
//				 if (tag.equals("s") || tag.equals("e")) {
//		                words.add(word);
//		                word = "";
//		            }
//				i++;
//				if(i == tagsandposesPre.length){
//					break;
//				}
//			}
//		}
//		for (String string : poses) {
//			System.out.println(string);
//		}
//		for (String string : words) {
//			System.out.println(string);
//		}
	}
	
	public static void find(int length, String[] str){
		int middle = (str.length - 1) / 2;
		String temp = "";
		for (int i = middle; i < middle+length; i++) {
			temp += str[i];	
		}
		if(temp.contains("null")){
			
		}else{
			System.out.println(temp);
		}

//
//		for (int i = middle-length+1; i < middle+1; i++) {			
//			String temp = "";
//			int begin = -1;
//			int end = -1;
//			for (int j = i; j < length+i; j++) {
//				temp += str[j];
//				begin = j - length + 1;
//				end = j;				
//			}
//			System.out.println(temp);
//			if(temp.contains("null")){
//				
//			}else{
//				if(begin == middle){
//					System.out.println("b");
//				}else if(end == middle){
//					System.out.println("e");
//				}else{
//					System.out.println("m");
//				}
//
//			}
//			
//			
//		}

		
	}
}
