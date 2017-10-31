package com.wxw.ner.sample;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseNews;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 过滤文本流得到想要的样式
 * @author 王馨苇
 *
 */
public class NERNewsSampleStream extends FilterObjectStream<String,NERNewsSample>{

	private static Logger logger = Logger.getLogger(NERNewsSampleStream.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERNewsSampleStream(ObjectStream<String> samples) {
		super(samples);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERNewsSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseNews(),sentence);
		NERNewsSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					//System.out.println(sentences);
					sample = context.parseNewsSample();;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {
						
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERNewsSample(new String[]{},new String[]{},new String[]{});
				}

				return sample;
			}else {
				sample = new NERNewsSample(new String[]{},new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}
