package com.wxw.ner.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NERCharacterSample extends AbstractNERSample{

	public List<String> tags;
	public List<String> characters;
	private String[][] addtionalContext;
	
	/**
	 * 构造
	 * @param words 词语
	 * @param tags 实体标记
	 */
	public NERCharacterSample(String[] characters,String[] tags){
		this(characters,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 */
	public NERCharacterSample(List<String> characters,List<String> tags){
		this(characters,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
	public NERCharacterSample(String[] characters,String[] tags,String[][] additionalContext){
		this(Arrays.asList(characters),Arrays.asList(tags),additionalContext);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
    public NERCharacterSample(List<String> characters,List<String> tags,String[][] additionalContext){
        super(characters,new ArrayList<>(),tags,additionalContext);
	}
 
    /**
     * 重写equals方法
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        } else if (obj instanceof NERCharacterSample) {
        	NERCharacterSample a = (NERCharacterSample) obj;

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
		String[] word = toWord(characters,tags);
		String[] tag = toNer(tags);
		String sample = "";
		for (int i = 0; i < word.length; i++) {
			sample += word[i]+"/"+tag[i]+" ";
		}
		return sample;
	}
}
