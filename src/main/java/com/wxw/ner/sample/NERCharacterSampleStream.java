package com.wxw.ner.sample;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseCharacter;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 为基于字的命名实体识别过滤文本流得到想要的样式
 * @author 王馨苇
 *
 */
public class NERCharacterSampleStream extends FilterObjectStream<String,NERWordOrCharacterSample>{

	//自定义日志记录器
	private static Logger logger = Logger.getLogger(NERCharacterSampleStream.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERCharacterSampleStream(ObjectStream<String> samples) {
		super(samples);
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERWordOrCharacterSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseCharacter());
		NERWordOrCharacterSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					//System.out.println(sentences);
					sample = context.parseSample(sentence);;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {
						
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERWordOrCharacterSample(new String[]{},new String[]{});
				}
				return sample;
			}else {
				sample = new NERWordOrCharacterSample(new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}