package com.wxw.ner.parse;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.NERParseStrategy;
import com.wxw.parse.NERParseWord;
import com.wxw.sample.NERWordOrCharacterSample;

/**
 * msra语料基于词语的解析测试
 * @author 王馨苇
 *
 */
public class NERParseWordTest {

	private NERWordOrCharacterSample sample;
	private NERParseStrategy strategy;
	private String sentence;
	private String[] wordresult;
	private String[] tagresult;
	
	@Before
	public void setUp(){
		sentence = "最大/o 的/o 龛/o 图/o 竟/o 有/o 八十多米/o ，/o 最小/o 的/o 也/o 有/o 十来米/o ，/o 还有/o 记载/o 宝顶山/ns 沿革/o 和/o 密宗/nr 史实/o 的/o 七通/o 碑刻/o 及/o 各种/o 题记/o 十七则/o 和/o 两座/o 保存/o 很/o 好/o 的/o 舍利/o 塔/o 。/o ";
		strategy = new NERParseWord();
		sample = strategy.parse(sentence);
		wordresult = new String[]{"最大","的","龛","图","竟","有","八十多米","，","最小","的","也","有","十来米","，","还有","记载","宝顶山","沿革","和","密宗","史实","的","七通","碑刻","及","各种","题记","十七则","和","两座","保存","很","好","的","舍利","塔","。"};
		tagresult = new String[]{"o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","s_ns","o","o","s_nr","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o"};
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(sample.getWords()),Arrays.asList(wordresult));
		assertEquals(Arrays.asList(sample.getTags()),Arrays.asList(tagresult));
	}
}
