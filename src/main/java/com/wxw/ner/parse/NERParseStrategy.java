package com.wxw.ner.parse;

import com.wxw.ner.sample.NERCharacterSample;
import com.wxw.ner.sample.NERWordAndPosSample;
import com.wxw.ner.sample.NERWordSample;

/**
 * 策略接口
 * @author 王馨苇
 *
 */
public interface NERParseStrategy {

	/**
	 * 为基于字的命名实体解析文本
	 * @param sentence
	 * @return
	 */
	public NERCharacterSample parse(String sentence);
	/**
	 * 为基于词性标注的命名实体解析文本
	 * @param sentence
	 * @return
	 */
	public NERWordAndPosSample parseNews(String sentence);
	/**
	 * 为基于分词的命名实体解析文本
	 * @param sentence
	 * @return
	 */
	public NERWordSample parseWord(String sentence);
}
