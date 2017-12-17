package com.wxw.namedentity;

/**
 * 命名实体类
 * @author 王馨苇
 *
 */
public class NamedEntity {

	private String type;
	private String string;
	private int start;
	private int end;
	private String[] words;

	public void setType(String type){
		this.type = type;
	}
	
	public void setString(String string){
		this.string = string;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
	
	public void setWords(String[] words){
		this.words = words;
	}
	
	/**
	 * 实体的类型
	 * @return
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * 实体字符串
	 * @return
	 */
	public String getString(){
		return this.string;
	}
	
	/**
	 * 实体起始位置
	 * @return
	 */
	public int getStart(){
		return this.start;
	}
	
	/**
	 * 实体结束位置
	 * @return
	 */
	public int getEnd(){
		return this.end;
	}
	
	/**
	 * 实体构成的词序列
	 * @return
	 */
	public String[] getWords(){
		return this.words;
	}
}
