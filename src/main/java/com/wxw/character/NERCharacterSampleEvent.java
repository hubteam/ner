package com.wxw.character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wxw.sample.NERWordOrCharacterSample;

import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;

/**
 * 为基于字的命名实体识别生成事件
 * @author 王馨苇
 *
 */
public class NERCharacterSampleEvent extends AbstractEventStream<NERWordOrCharacterSample>{

	private NERCharacterContextGenerator generator;
	
	/**
	 * 构造
	 * @param samples 样本流
	 * @param generator 上下文产生器
	 */
	public NERCharacterSampleEvent(ObjectStream<NERWordOrCharacterSample> samples,NERCharacterContextGenerator generator) {
		super(samples);
		this.generator = generator;
	}
	
	@Override
	protected Iterator<Event> createEvents(NERWordOrCharacterSample sample) {
		String[] words = sample.getWords();
		String[] tags = sample.getTags();
		String[][] ac = sample.getAditionalContext();
		List<Event> events = generateEvents(words, tags, ac);
        return events.iterator();
	}

	/**
	 * 产生事件
	 * @param words 词语
	 * @param tags 命名实体标记
	 * @param ac
	 * @return
	 */
	private List<Event> generateEvents(String[] words, String[] tags, String[][] ac) {
		List<Event> events = new ArrayList<Event>(words.length);
		for (int i = 0; i < words.length; i++) {			
			//产生事件的部分
			String[] context = generator.getContext(i, words, tags, ac);
			events.add(new Event(tags[i], context));			          
		}
		return events;
	}
}