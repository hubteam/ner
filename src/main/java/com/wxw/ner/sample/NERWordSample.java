package com.wxw.ner.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NERWordSample extends AbstractNERSample{
	public List<String> tags;
	public List<String> words;
	private String[][] addtionalContext;
	
	/**
	 * 构造
	 * @param words 词语
	 * @param tags 实体标记
	 */
	public NERWordSample(String[] words,String[] tags){
		this(words,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 */
	public NERWordSample(List<String> words,List<String> tags){
		this(words,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
	public NERWordSample(String[] words,String[] tags,String[][] additionalContext){
		this(Arrays.asList(words),Arrays.asList(tags),additionalContext);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
    public NERWordSample(List<String> words,List<String> tags,String[][] additionalContext){
        super(words,new ArrayList<>(),tags,additionalContext);
	}
    
   

    /**
     * 重写equals方法
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        } else if (obj instanceof NERWordSample) {
        	NERWordSample a = (NERWordSample) obj;

            return Arrays.equals(getWords(), a.getWords())
                    && Arrays.equals(getTags(), a.getTags());
        } else {
            return false;
        }
	}
	
	/**
	 * 得到和输入样本一致的样式
	 * @return
	 */
	public String toString() {
		String[] word = toWord(words,tags);
		String[] tag = toNer(tags);
		String sample = "";
		for (int i = 0; i < word.length; i++) {
			sample += word[i]+"/"+tag[i]+" ";
		}
		return sample;
	}
}

