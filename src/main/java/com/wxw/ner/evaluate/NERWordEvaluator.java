package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERCharacterSample;
import com.wxw.ner.sample.NERWordSample;
import com.wxw.ner.wordandpos.model.NERWordAndPosME;
import com.wxw.word.model.NERWordME;

import opennlp.tools.util.eval.Evaluator;

/**
 * 基于分词的命名实体分析评估器
 * @author 王馨苇
 *
 */
public class NERWordEvaluator extends Evaluator<NERWordSample>{

	private NERWordME tagger;
	private NERMeasure measure;
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERWordEvaluator(NERWordME tagger) {
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERWordEvaluator(NERWordME tagger,NERWordEvaluateMonitor... evaluateMonitors) {
		super(evaluateMonitors);
		this.tagger = tagger;
	}
	
	/**
	 * 设置评估指标的对象
	 * @param measure 评估指标计算的对象
	 */
	public void setMeasure(NERMeasure measure){
		this.measure = measure;
	}
	
	/**
	 * 得到评估的指标
	 * @return
	 */
	public NERMeasure getMeasure(){
		return this.measure;
	}
	
	/**
	 * 评估得到指标
	 * @param reference 参考的语料
	 */
	@Override
	protected NERWordSample processSample(NERWordSample sample) {
		String[] wordsRef = sample.getWords();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(wordsRef, acRef);
		
		String[] tagsRef = NERCharacterSample.toPos(wordsAndtagsRef);
		String[] nerRef = NERCharacterSample.toWord(wordsRef, wordsAndtagsRef);
		String[] tagsPre = NERCharacterSample.toPos(wordsAndtagsPre);
		String[] nerPre = NERCharacterSample.toWord(wordsRef, wordsAndtagsPre);
		
		NERWordSample prediction = new NERWordSample(wordsRef, wordsAndtagsPre);
		measure.update(nerRef, tagsRef, nerPre, tagsPre);
		for (int i = 0; i < tagsRef.length; i++) {
			System.out.print(nerRef[i]+"/"+tagsRef[i]);
		}
		System.out.println();
		for (int i = 0; i < tagsPre.length; i++) {
			System.out.print(nerPre[i]+"/"+tagsPre[i]);
		}
		System.out.println();

		return prediction;
	}
}
