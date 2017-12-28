package com.wxw.character;

import java.util.HashSet;

/**
 * 特征工具类
 * @author 王馨苇
 *
 */
public class FeatureTools {


/*
 * 新增的特征进行选择
*/
	static HashSet<Character> hsdigit = new HashSet<Character>();
	static HashSet<Character> hspunctuation = new HashSet<Character>();
	static HashSet<Character> hsalbdigit = new HashSet<Character>();
	
	static{
		//罗列了半角和全角的情况
		String digits = "０１２３４５６７８９0123456789零一二三四五六七八九十";
		String albdigits = "０１２３４５６７８９0123456789";
//		asc_punc_str=u"!\"&\'()+,-./:;<=>?[\\]^_`{|}~"
//		chi_punc_str=u"。？！，、；：“”‘’（）{}【】—…《》「」『』〈〉＆"
		//包含中文英文的半角下的标点符号【不包含特殊字符】
		//String punctuation = "!\"&\'()+,-./:;<=>?[\\]^_`{|}~。？！，、；：“”‘’（）{}【】—…《》「」『』〈〉";
		String punctuationE = "',:--!-()[]{}<>/.?\";";
		String punctuationC = "‘’，：——！-（）【】{}《》/。？“”；…「」『』";
		String punctuation = punctuationC + punctuationE;
		
		for (int i = 0; i < digits.length(); i++) {
			hsdigit.add(digits.charAt(i));
		}
		for (int i = 0; i < punctuation.length(); i++) {
			hspunctuation.add(punctuation.charAt(i));
		}
		
		for (int i = 0; i < albdigits.length(); i++) {
			hsalbdigit.add(albdigits.charAt(i));
		}
	}
	
	//判断是否是数字【中文数字，阿拉伯数字（全角和半角）】
	public static boolean isDigit(char c){
		if(hsdigit.contains(c)){
			return true;
		}else{
			return false;
		}	
	}
	
	public static boolean isAlbDigit(char c){
		if(hsalbdigit.contains(c)){
			return true;
		}else{
			return false;
		}	
	}
	//判断是否为年月日
	public static boolean isDate(char c){
		if(c=='年' || c=='月' || c=='日'){
			return true;
		}else{
			return false;
		}	
	}
	
	public static boolean isTime(char c){
		if(c=='时' || c=='分' || c=='秒'){
			return true;
		}else{
			return false;
		}	
	}
	
	//判断是否为应为字母（大小写，全角半角）【全角半角的差别在于ASCII码】
	public static boolean isLetter(char c){
		if((c>=65 && c<=90) || (c>=97 && c<=122) || (c>=65345 && c<=65370) || (c>=65313 && c<=65338)){
			return true;
		}else{
			return false;
		}
	}
	
	//全角转半角
		public static String strq2b(String text){
			return String.valueOf(strq2b(text.toCharArray()));
		}
		
		public static char[] strq2b(char[] text){
			char[] newText = new char[text.length];
			for(int i=0;i<text.length;i++){
				char ch=text[i];
				if(ch==12288)
					newText[i]=' ';
				else if(ch>=65281 && ch<=65374){
					ch-=65248;
					newText[i]=ch;
				}else{
					newText[i]=ch;
				}
			}
			return newText;
		}
	
	public static String featureType(String c){
		if(isDigit(c.toCharArray()[0])){
			return "1";
		}else if(isDate(c.toCharArray()[0])){
			return "2";
		}else if(isLetter(c.toCharArray()[0])){
			return "3";
		}else{
			return "4";
		}
	}
	
	//中文状态下的标点符号【前提：全部转为全角状态，再来判断是否是标点】
		public static boolean isChinesePunctuation(String c) {
			if(hspunctuation.contains(c.toCharArray()[0])){
				return true;
			}else{
				return false;
			}
	    }
}

