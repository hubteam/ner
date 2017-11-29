package com.wxw.ner.sample;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseCharacterSingle;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

public class NERCharacterSampleStreamSingle extends FilterObjectStream<String,NERCharacterSample>{

	private static Logger logger = Logger.getLogger(NERCharacterSampleStreamSingle.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERCharacterSampleStreamSingle(ObjectStream<String> samples) {
		super(samples);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERCharacterSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseCharacterSingle(),sentence);
		NERCharacterSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					//System.out.println(sentences);
					sample = context.parseSample();;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {
						
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERCharacterSample(new String[]{},new String[]{});
				}

				return sample;
			}else {
				sample = new NERCharacterSample(new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}
