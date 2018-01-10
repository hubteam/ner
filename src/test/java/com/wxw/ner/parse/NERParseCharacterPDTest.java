package com.wxw.ner.parse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.NERParseCharacterPD;
import com.wxw.parse.NERParseStrategy;
import com.wxw.sample.NERWordOrCharacterSample;

/**
 * 人民日报语料基于字的解析测试
 * @author 王馨苇
 *
 */
public class NERParseCharacterPDTest {

	private NERWordOrCharacterSample sample;
	private NERParseStrategy strategy;
	private String sentence;
	private String[] wordresult;
	private String[] tagresult;
	
	@Before
	public void setp(){
		strategy = new NERParseCharacterPD();
		sentence = "19980128-01-007-003/m  记者/n  翻山越岭/i  在/p  年前/t  赶到/v  了/u  [广西/ns  壮族/nz  自治区/n]ns  灵川县/ns  九屋乡/ns  瑶山/n  老寨/n  ，/w  过年/v  的/u  浓浓/z  氛围/n  弥漫/v  了/u  整个/b  村寨/n  ：/w  鞭炮声/n  响/v  个/q  不/d  停/v  ，/w  锣鼓声/n  悦耳/a  动听/a  。/w ";
		sample = strategy.parse(sentence);
		wordresult = new String[]{"记","者","翻","山","越","岭","在","年","前","赶","到","了","广","西","壮","族","自","治","区","灵","川","县","九","屋","乡","瑶","山","老","寨","，","过","年","的","浓","浓","氛","围","弥","漫","了","整","个","村","寨","：","鞭","炮","声","响","个","不","停","，","锣","鼓","声","悦","耳","动","听","。"};
		tagresult = new String[]{"o","o","o","o","o","o","o","o","o","o","o","o","b_ns","m_ns","m_ns","m_ns","m_ns","m_ns","e_ns","b_ns","m_ns","m_ns","m_ns","m_ns","e_ns","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o"};
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(sample.getWords()),Arrays.asList(wordresult));
		assertEquals(Arrays.asList(sample.getTags()),Arrays.asList(tagresult));
	}
}
