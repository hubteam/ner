package com.wxw.ner.evaluate;

import com.wxw.ner.msr.model.NERMsrME;
import com.wxw.ner.msr.model.NERMsrMESingle;
import com.wxw.ner.sample.NERMsrSample;

import opennlp.tools.util.eval.Evaluator;

public class NERMsrEvaluator extends Evaluator<NERMsrSample>{

	private NERMsrME tagger;
	private NERMsrMESingle taggerSingle;
	private NERMeasure measure;
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERMsrEvaluator(NERMsrME tagger) {
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERMsrEvaluator(NERMsrME tagger,NERMsrEvaluateMonitor... evaluateMonitors) {
		super(evaluateMonitors);
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERMsrEvaluator(NERMsrMESingle taggerSingle) {
		this.taggerSingle = taggerSingle;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERMsrEvaluator(NERMsrMESingle taggerSingle,NERMsrEvaluateMonitor... evaluateMonitors) {
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
	protected NERMsrSample processSample(NERMsrSample sample) {
		String[] charactersRef = sample.getWords();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(charactersRef, acRef);
		
		String[] tagsRef = NERMsrSample.toPos(wordsAndtagsRef);
		String[] wordsRef = NERMsrSample.toWord(charactersRef, wordsAndtagsRef);
		String[] tagsPre = NERMsrSample.toPos(wordsAndtagsPre);
		String[] wordsPre = NERMsrSample.toWord(charactersRef, wordsAndtagsPre);
		
		NERMsrSample prediction = new NERMsrSample(charactersRef, wordsAndtagsPre);
		measure.update(wordsRef, tagsRef, wordsPre, tagsPre);
		for (int i = 0; i < tagsRef.length; i++) {
			System.out.print(wordsRef[i]+"/"+tagsRef[i]);
		}
		System.out.println();
		for (int i = 0; i < wordsAndtagsPre.length; i++) {
			System.out.print(wordsAndtagsPre[i]);
		}
		System.out.println();
		for (int i = 0; i < wordsPre.length; i++) {
			System.out.print(wordsPre[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < tagsPre.length; i++) {
			System.out.print(tagsPre[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < tagsPre.length; i++) {
			System.out.print(wordsPre[i]+"/"+tagsPre[i]);
		}
		System.out.println();

		return prediction;
	} 
}