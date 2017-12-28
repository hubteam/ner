package com.wxw.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NERWordAndPosSample extends NERWordOrCharacterSample{

	public List<String> poses;
	private String[][] addtionalContext;
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 */
	public NERWordAndPosSample(String[] words,String[] poses, String[] tags){
		this(words,poses,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 */
	public NERWordAndPosSample(List<String> words,List<String> poses,List<String> tags){
		this(words,poses,tags,null);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
	public NERWordAndPosSample(String[] words,String[] poses, String[] tags,String[][] additionalContext){
		this(Arrays.asList(words),Arrays.asList(poses),Arrays.asList(tags),additionalContext);
	}
	
	/**
	 * 构造
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 实体标记
	 * @param additionalContext 额外的信息
	 */
    public NERWordAndPosSample(List<String> words,List<String> poses,List<String> tags,String[][] additionalContext){
        super(words,tags,additionalContext);
        this.poses = poses;
	}
    
    public String[] getPoses(){
    	return poses.toArray(new String[poses.size()]);
    }
    
	/**
	 * 得到和输入样本一致的样式
	 * @return
	 */
	public String toString() {
		String[] word = toWordAndPos(words,poses,tags);
		String[] tag = toNer(tags);
		String sample = "";
		for (int i = 0; i < word.length; i++) {
			sample += word[i]+"/"+tag[i]+" ";
		}
		return sample;
	}
	
	/**
     * 得到对应的分词序列
     * @param characters 词语序列
     * @param poses 词性标记
     * @param tagsandposesPre 字的边界_实体标记 这种格式组成的序列
     * @return
     */
	public static String[] toWordAndPos(String[] characters, String[] poses, String[] tagsandposesPre) {
		String word = new String();
        ArrayList<String> words = new ArrayList<String>();       
		int i = 0;
		while(i < tagsandposesPre.length){
			if(tagsandposesPre[i].equals("o")){				
				while(tagsandposesPre[i].equals("o")){
					word += characters[i]+"/"+poses[i];
					i++;
					if(i == tagsandposesPre.length){
						break;
					}
				}
				words.add(word);
				word = "";
			}else{	
				String tag = tagsandposesPre[i].split("_")[0];
				if (tag.equals("s") || tag.equals("e")) {
					word += characters[i]+"/"+poses[i]; 
					words.add(word);
					word = ""; 
				}else if(tag.equals("b") || tag.equals("m")){	 
					word += characters[i]+"/"+poses[i]+" ";
				}
				i++;
				if(i == tagsandposesPre.length){
					break;
				}
			}
		}
        return words.toArray(new String[words.size()]);
	}
	
	/**
     * 得到对应的分词序列
     * @param characters 词语序列
     * @param poses 词性标记
     * @param tagsandposesPre 字的边界_实体标记 这种格式组成的序列
     * @return
     */
	public static String[] toWordAndPos(List<String> characters, List<String> poses, List<String> tagsandposesPre) {
		return toWordAndPos(characters.toArray(new String[characters.size()]),
				poses.toArray(new String[poses.size()]),
				tagsandposesPre.toArray(new String[tagsandposesPre.size()]));
	}
}

