package com.wxw.ner.sample;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseWordAndPos;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 为基于词性标注的命名实体识别过滤文本流得到想要的样式
 * @author 王馨苇
 *
 */
public class NERWordAndPosSampleStream extends FilterObjectStream<String,NERWordAndPosSample>{

	private static Logger logger = Logger.getLogger(NERWordAndPosSampleStream.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERWordAndPosSampleStream(ObjectStream<String> samples) {
		super(samples);
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERWordAndPosSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseWordAndPos());
		NERWordAndPosSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					sample = context.parseNewsSample(sentence);;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {					
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERWordAndPosSample(new String[]{},new String[]{},new String[]{});
				}
				return sample;
			}else {
				sample = new NERWordAndPosSample(new String[]{},new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}
