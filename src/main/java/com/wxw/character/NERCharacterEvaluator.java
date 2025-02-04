package com.wxw.character;

import com.wxw.evaluate.NEREvaluateMonitor;
import com.wxw.evaluate.NERMeasure;
import com.wxw.sample.NERWordOrCharacterSample;

import opennlp.tools.util.eval.Evaluator;

public class NERCharacterEvaluator extends Evaluator<NERWordOrCharacterSample>{

	private NERCharacterME tagger;
	@SuppressWarnings("unused")
	private NERCharacterMESingle taggerSingle;
	private NERMeasure measure;
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERCharacterEvaluator(NERCharacterME tagger) {
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERCharacterEvaluator(NERCharacterME tagger,NEREvaluateMonitor... evaluateMonitors) {
		super(evaluateMonitors);
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERCharacterEvaluator(NERCharacterMESingle taggerSingle) {
		this.taggerSingle = taggerSingle;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERCharacterEvaluator(NERCharacterMESingle taggerSingle,NEREvaluateMonitor... evaluateMonitors) {
		super(evaluateMonitors);
		this.taggerSingle = taggerSingle;
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
	protected NERWordOrCharacterSample processSample(NERWordOrCharacterSample sample) {
		String[] charactersRef = sample.getWords();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(charactersRef, acRef);
		
		String[] tagsRef = NERWordOrCharacterSample.toNer(wordsAndtagsRef);
		String[] wordsRef = NERWordOrCharacterSample.toWord(charactersRef, wordsAndtagsRef);
		String[] tagsPre = NERWordOrCharacterSample.toNer(wordsAndtagsPre);
		String[] wordsPre = NERWordOrCharacterSample.toWord(charactersRef, wordsAndtagsPre);
		
		NERWordOrCharacterSample prediction = new NERWordOrCharacterSample(charactersRef, wordsAndtagsPre);
		measure.update(wordsRef, tagsRef, wordsPre, tagsPre);

		return prediction;
	} 
}