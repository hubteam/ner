package com.wxw.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.wxw.sample.NERWordOrCharacterSample;

import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * 对人民日报语料做基于字的解析
 * @author 王馨苇
 *
 */
public class NERParseCharacterPD implements NERParseStrategy {

	private ArrayList<String> characters = new ArrayList<String>();
    private ArrayList<String> tags = new ArrayList<String>();
	@Override
	public NERWordOrCharacterSample parse(String sentence) {
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
				String temp = "";//存放词语的拼接
				word = word.substring(1);
				while(!pos.contains("]")){
					temp += word;
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
				temp += word;
				if(tag.equals("nr")){
					ner = "nr";
					add(temp,ner);
				}else if(tag.equals("ns")){
					ner = "ns";
					add(temp,ner);
				}else if(tag.equals("nt")){
					ner = "nt";
					add(temp,ner);
				}else if(tag.equals("nz")){
					ner = "nz";
					add(temp,ner);
				}
			}else if(pos.equals("nr")){
				String temp = "";
				ner = "nr";
				while(pos.equals("nr")){
					temp += word;
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					if(queue.size() == 2){
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(temp,ner);
				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("ns")){
				String temp = "";
				ner = "ns";
				while(pos.equals("ns")){
					temp += word;
					if((i+1) < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(temp,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("nt")){
				String temp = "";
				ner = "nt";
				while(pos.equals("nt")){
					temp += word;
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(temp,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("nz")){
				String temp = "";
				ner = "nz";
				while(pos.equals("nz")){
					queue.offer(word);
					if(i+1 < str.length){
						i++;
					}else{
						break;
					}
					word = getWordAndPos(str,i)[0];
					pos = getWordAndPos(str,i)[1];	
				}
				add(temp,ner);

				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else{
				for (int j = 0; j < word.length(); j++) {
					characters.add(word.charAt(j)+"");
					tags.add("o");
				}
			}
			
			if(i+1 < str.length){
				i++;
			}else{
				break;
			}
		}
		return new NERWordOrCharacterSample(characters,tags);
	}

	/**
	 * 获得词语和词性数组
	 * @param str 词语和词性
	 * @param i 当前位置
	 * @return
	 */
	public String[] getWordAndPos(String[] str, int i){
		String[] temp = str[i].split("/");
		return temp;
	}
	
	/**
	 * 解析出字符和字符的标记
	 * @param str 词语拼接的字符串
	 * @param tag 标价
	 */
	public void add(String str,String tag){
		if(str.length() == 1){
			characters.add(str);
			tags.add("s_"+tag);
		}else if(str.length() == 2){
			characters.add(str.toCharArray()[0]+"");
			tags.add("b_"+tag);
			characters.add(str.toCharArray()[1]+"");
			tags.add("e_"+tag);
		}else if(str.length() > 2){
			char[] c = str.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if(i == 0){
					characters.add(c[0]+"");
					tags.add("b_"+tag);
				}else if(i == c.length -1){
					characters.add(c[c.length-1]+"");
					tags.add("e_"+tag);
				}else{
					characters.add(c[i]+"");
					tags.add("m_"+tag);
				}
			}
		}
	}
}
