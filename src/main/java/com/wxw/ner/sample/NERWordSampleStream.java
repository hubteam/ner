package com.wxw.ner.sample;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseWord;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 为基于分词的命名实体识别过滤文本流得到想要的样式
 * @author 王馨苇
 *
 */
public class NERWordSampleStream extends FilterObjectStream<String,NERWordSample>{

	private static Logger logger = Logger.getLogger(NERWordAndPosSampleStream.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERWordSampleStream(ObjectStream<String> samples) {
		super(samples);
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERWordSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseWord());
		NERWordSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					sample = context.parseWordSample(sentence);;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERWordSample(new String[]{},new String[]{});
				}
				return sample;
			}else {
				sample = new NERWordSample(new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}
