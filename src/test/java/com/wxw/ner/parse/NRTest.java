package com.wxw.ner.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public class NRTest {

	private static Set<String> wordSet = new HashSet<String>();

	public static Set<String> getWords(String path , String code){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path)),"gbk"));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.replaceAll("\\s","").replaceAll("\n","");
				if(line.length() != 1){
					wordSet.add(line);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return wordSet;	
	}
	
	public static void main(String[] args) {
		
	}
}
