package com.wxw.ner.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wxw.ner.news.feature.NERNewsContextGenerator;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;

public class NERNewsSampleEvent extends AbstractEventStream<NERNewsSample>{


	private NERNewsContextGenerator generator;
	
	/**
	 * 构造
	 * @param samples 样本流
	 * @param generator 上下文产生器
	 */
	public NERNewsSampleEvent(ObjectStream<NERNewsSample> samples,NERNewsContextGenerator generator) {
		super(samples);
		this.generator = generator;
	}

	@Override
	protected Iterator<Event> createEvents(NERNewsSample sample) {
		String[] words = sample.getWords();
		String[] poses = sample.getPoses();
		String[] tags = sample.getTags();
		String[][] ac = sample.getAditionalContext();
		List<Event> events = generateEvents(words,poses, tags, ac);
        return events.iterator();
	}

	/**
	 * 产生事件
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 命名实体标记
	 * @param ac
	 * @return
	 */
	private List<Event> generateEvents(String[] words, String[] poses, String[] tags, String[][] ac) {
		List<Event> events = new ArrayList<Event>(words.length);

		for (int i = 0; i < words.length; i++) {
			
			//产生事件的部分
			String[] context = generator.getContext(i, words, poses, tags, ac);

            events.add(new Event(tags[i], context));
		}
		return events;
	}

}
