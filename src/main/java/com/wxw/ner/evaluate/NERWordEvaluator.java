package com.wxw.ner.evaluate;

import com.wxw.ner.sample.AbstractNERSample;
import com.wxw.ner.sample.NERWordSample;
import com.wxw.word.model.NERWordME;

import opennlp.tools.util.eval.Evaluator;

/**
 * 基于分词的命名实体分析评估器
 * @author 王馨苇
 *
 */
public class NERWordEvaluator extends Evaluator<AbstractNERSample>{

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
	public NERWordEvaluator(NERWordME tagger,NEREvaluateMonitor... evaluateMonitors) {
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
	protected NERWordSample processSample(AbstractNERSample sample) {
		String[] wordsRef = sample.getWords();
		for (int i = 0; i < wordsRef.length; i++) {
			System.out.print(wordsRef[i]+" ");
		}
		System.out.println();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(wordsRef, acRef);
		for (int i = 0; i < wordsAndtagsPre.length; i++) {
			System.out.print(wordsAndtagsPre[i]+" ");
		}
		System.out.println();
		String[] tagsRef = AbstractNERSample.toNer(wordsAndtagsRef);
		String[] nerRef = AbstractNERSample.toWord(wordsRef, wordsAndtagsRef);
		String[] tagsPre = AbstractNERSample.toNer(wordsAndtagsPre);
		String[] nerPre = AbstractNERSample.toWord(wordsRef, wordsAndtagsPre);
		
		NERWordSample prediction = new NERWordSample(wordsRef, wordsAndtagsPre);
		measure.update(nerRef, tagsRef, nerPre, tagsPre);
		return prediction;
	}
}
