package com.wxw.ner.sample;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.ner.parse.NERParseContext;
import com.wxw.ner.parse.NERParseMSR;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 过滤文本流得到想要的样式
 * @author 王馨苇
 *
 */
public class NERMsrSampleStream extends FilterObjectStream<String,NERMsrSample>{

	//自定义日志记录器
	private static Logger logger = Logger.getLogger(NERMsrSampleStream.class.getName());
	/**
	 * 构造
	 * @param samples 样本流
	 */
	public NERMsrSampleStream(ObjectStream<String> samples) {
		super(samples);
	}

	/**
	 * 读取样本进行解析
	 * @return 
	 */	
	public NERMsrSample read() throws IOException {
		String sentence = samples.read();
		NERParseContext context = new NERParseContext(new NERParseMSR(),sentence);
		NERMsrSample sample = null;
		if(sentence != null){
			if(sentence.compareTo("") != 0){
				try{
					//System.out.println(sentences);
					sample = context.parseSample();;
				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {
						
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }
					sample = new NERMsrSample(new String[]{},new String[]{});
				}

				return sample;
			}else {
				sample = new NERMsrSample(new String[]{},new String[]{});
				return sample;
			}
		}
		else{
			return null;
		}
	}
}