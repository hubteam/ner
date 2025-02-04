package com.wxw.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 读取人名，地名，机构名词典
 * @author 王馨苇
 *
 */
public class ReadAdditionalDitionary {
	
	private static Set<String> wordSet = new HashSet<String>();

	public static Set<String> getWords(String path , String code){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),code));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.replaceAll("\\s","").replaceAll("\n","");
				if(line.length() != 1){
					wordSet.add(line);
				}
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return wordSet;	
	}
	
	public static DictionaryWrapper getLengthAndWords(String path , String code){
		DictionaryWrapper wrapper = null;
		try {		
			//记录词语的长度的个数
			TreeMap<Integer,Integer> hs = new TreeMap<Integer, Integer>();			 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),code));
			String line = null;
			int count = 0;
			int size = 0;
			while((line = br.readLine()) != null){
				line = line.replaceAll("\\s","").replaceAll("\n","");
				if(line.length() != 1){
					wordSet.add(line);
					Integer i = hs.get(line.length());
					if(i == null){
						count++;
						hs.put(line.length(), Integer.valueOf(1));
					}else{
						count++;
						hs.put(line.length(),Integer.valueOf(i.intValue() + 1));
					}
				}
			}
			br.close();

			int length = 0;
	        for (Entry<Integer, Integer> e: hs.entrySet()) {  
	        	size += e.getValue();
	        	
	        	if((double)size/count >= 0.99){
	        		length = e.getKey();
	        		break;
	        	}
	        } 
	        wrapper = new DictionaryWrapper(length, wordSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return wrapper;	
	}
}
