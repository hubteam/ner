package com.wxw.ner.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NERWordSample {
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
    	
        this.tags = Collections.unmodifiableList(tags);
        this.words = Collections.unmodifiableList(words);

        String[][] ac;
        if (additionalContext != null) {
            ac = new String[additionalContext.length][];

            for (int i = 0; i < additionalContext.length; i++) {
                ac[i] = new String[additionalContext[i].length];
                System.arraycopy(additionalContext[i], 0, ac[i], 0,
                        additionalContext[i].length);
            }
        } else {
            ac = null;
        }
        this.addtionalContext = ac;
	}
    
    /**
     * 获得词语
     * @return 
     */
    public String[] getWords(){
    	return this.words.toArray(new String[words.size()]);
    }
    
    /**
     * 获得实体标记
     * @return
     */
    public String[] getTags(){
    	return this.tags.toArray(new String[tags.size()]);
    }
    /**
     * 获得额外的信息
     * @return
     */
    public String[][] getAditionalContext(){
    	return this.addtionalContext;
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
	public String toSample() {
		String[] word = toWord(words,tags);
		String[] tag = toPos(tags);
		String sample = "";
		for (int i = 0; i < word.length; i++) {
			sample += word[i]+"/"+tag[i]+" ";
		}
		return sample;
	}
	
	/**
     * 得到对应的命名实体标注序列
     * @param tagsandposesPre 字的边界_命名实体标注 这种格式组成的序列
     * @return
     */
	public static String[] toPos(String[] tagsandposesPre) {
		List<String> poses = new ArrayList<String>();
		int i = 0;
		while(i < tagsandposesPre.length){
			if(tagsandposesPre[i].equals("o")){
				poses.add(tagsandposesPre[i]);
				
				while(tagsandposesPre[i].equals("o")){
					i++;
					if(i == tagsandposesPre.length){
						break;
					}
				}				
			}else{
				String tag = tagsandposesPre[i].split("_")[0];
				String pos = tagsandposesPre[i].split("_")[1];
				if(tag.equals("b")){
					poses.add(pos);					
				}else if(tag.equals("m")){
					
				}else if(tag.equals("e")){
					
				}else if(tag.equals("s")){
					poses.add(pos);
				}
				i++;
				if(i == tagsandposesPre.length){
					break;
				}
			}
		}
		return poses.toArray(new String[poses.size()]);
	}
	
	/**
     * 得到对应的分词序列
     * @param characters 字符序列
     * @param tagsandposesPre 字的边界_词性 这种格式组成的序列
     * @return
     */
	public static String[] toWord(String[] characters, String[] tagsandposesPre) {
		String word = new String();
        ArrayList<String> words = new ArrayList<String>();       
		int i = 0;
		while(i < tagsandposesPre.length){
			if(tagsandposesPre[i].equals("o")){				
				while(tagsandposesPre[i].equals("o")){
					word += characters[i];
					i++;
					if(i == tagsandposesPre.length){
						break;
					}
				}
				words.add(word);
				word = "";
			}else{
				word += characters[i];
				String tag = tagsandposesPre[i].split("_")[0];

				 if (tag.equals("s") || tag.equals("e")) {
		                words.add(word);
		                word = "";
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
     * 得到对应的命名实体标注序列
     * @param tags 字的边界_命名实体标注 这种格式组成的序列
     * @return
     */
	public String[] toPos(List<String> tags) {
		List<String> poses = new ArrayList<String>();
		int i = 0;
		String[] tagsandposesPre = tags.toArray(new String[tags.size()]);
		while(i < tagsandposesPre.length){
			if(tagsandposesPre[i].equals("o")){
				poses.add(tagsandposesPre[i]);
				
				while(tagsandposesPre[i].equals("o")){
					i++;
					if(i == tagsandposesPre.length){
						break;
					}
				}				
			}else{
				String tag = tagsandposesPre[i].split("_")[0];
				String pos = tagsandposesPre[i].split("_")[1];
				if(tag.equals("b")){
					poses.add(pos);					
				}else if(tag.equals("m")){
					
				}else if(tag.equals("e")){
					
				}else if(tag.equals("s")){
					poses.add(pos);
				}
				i++;
				if(i == tagsandposesPre.length){
					break;
				}
			}
		}
		return poses.toArray(new String[poses.size()]);
	}
	
	/**
     * 得到对应的分词序列
     * @param characters 字符序列
     * @param tagsandposesPre 字的边界_词性 这种格式组成的序列
     * @return
     */
	public String[] toWord(List<String> characters, List<String> tagsandposesPre) {
		String word = new String();
        ArrayList<String> words = new ArrayList<String>();       
        String[] character = characters.toArray(new String[characters.size()]);
        String[] tagandposPre = tagsandposesPre.toArray(new String[tagsandposesPre.size()]);
		int i = 0;
		while(i < tagandposPre.length){
			if(tagandposPre[i].equals("o")){				
				while(tagandposPre[i].equals("o")){
					word += character[i];
					i++;
					if(i == tagandposPre.length){
						break;
					}
				}
				words.add(word);
				word = "";
			}else{
				word += character[i];
				String tag = tagandposPre[i].split("_")[0];

				 if (tag.equals("s") || tag.equals("e")) {
		                words.add(word);
		                word = "";
		            }
				i++;
				if(i == tagandposPre.length){
					break;
				}
			}
		}
        return words.toArray(new String[words.size()]);
	}
}

