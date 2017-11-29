package com.wxw.ner.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.wxw.ner.sample.NERCharacterSample;
import com.wxw.ner.sample.NERWordAndPosSample;
import com.wxw.ner.sample.NERWordSample;

import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * 为基于分词的命名实体识别解析文本
 * @author 王馨苇
 *
 */
public class NERParseWord implements NERParseStrategy{
	
	private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> tags = new ArrayList<String>();

	/**
	 * 解析文本，，基于分词的
	 */
	@Override
	public NERWordSample parseWord(String sentence) {
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
				add(queue,ner);
				if(i == str.length-1){
					
				}else{
					i--;
				}
			}else if(pos.equals("ns")){
				ner = "ns";
				while(pos.equals("ns")){
					queue.offer(word);
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
		return new NERWordSample(words,tags);
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
	 * 根据当前队列的长度为当前队列中的词语增加标记
	 * @param queue 词语队列
	 * @param tag 标价
	 */
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
	
	@Override
	public NERCharacterSample parse(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NERWordAndPosSample parseNews(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

}
