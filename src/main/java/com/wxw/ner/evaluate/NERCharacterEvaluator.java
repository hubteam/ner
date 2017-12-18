package com.wxw.ner.evaluate;

import com.wxw.ner.character.model.NERCharacterME;
import com.wxw.ner.character.model.NERCharacterMESingle;
import com.wxw.ner.sample.AbstractNERSample;
import com.wxw.ner.sample.NERCharacterSample;

import opennlp.tools.util.eval.Evaluator;

public class NERCharacterEvaluator extends Evaluator<AbstractNERSample>{

	private NERCharacterME tagger;
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
	protected NERCharacterSample processSample(AbstractNERSample sample) {
		String[] charactersRef = sample.getWords();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(charactersRef, acRef);
		
		String[] tagsRef = AbstractNERSample.toNer(wordsAndtagsRef);
		String[] wordsRef = AbstractNERSample.toWord(charactersRef, wordsAndtagsRef);
		String[] tagsPre = AbstractNERSample.toNer(wordsAndtagsPre);
		String[] wordsPre = AbstractNERSample.toWord(charactersRef, wordsAndtagsPre);
		
		NERCharacterSample prediction = new NERCharacterSample(charactersRef, wordsAndtagsPre);
		measure.update(wordsRef, tagsRef, wordsPre, tagsPre);
//		for (int i = 0; i < tagsRef.length; i++) {
//			System.out.print(wordsRef[i]+"/"+tagsRef[i]);
//		}
//		System.out.println();
//		for (int i = 0; i < wordsAndtagsPre.length; i++) {
//			System.out.print(wordsAndtagsPre[i]);
//		}
//		System.out.println();
//		for (int i = 0; i < wordsPre.length; i++) {
//			System.out.print(wordsPre[i]+" ");
//		}
//		System.out.println();
//		for (int i = 0; i < tagsPre.length; i++) {
//			System.out.print(tagsPre[i]+" ");
//		}
//		System.out.println();
//		for (int i = 0; i < tagsPre.length; i++) {
//			System.out.print(wordsPre[i]+"/"+tagsPre[i]);
//		}
//		System.out.println();

		return prediction;
	} 
}