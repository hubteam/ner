package com.wxw.ner.evaluate;

import com.wxw.ner.news.model.NERNewsME;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.util.eval.Evaluator;

/**
 * 评估器
 * @author 王馨苇
 *
 */
public class NERNewsEvaluator extends Evaluator<NERNewsSample>{

	private NERNewsME tagger;
	private NERMeasure measure;
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERNewsEvaluator(NERNewsME tagger) {
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERNewsEvaluator(NERNewsME tagger,NERNewsEvaluateMonitor... evaluateMonitors) {
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
	protected NERNewsSample processSample(NERNewsSample sample) {
		String[] charactersRef = sample.getWords();
		String[] posRef = sample.getPoses();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(charactersRef, posRef, acRef);
		
		String[] tagsRef = NERNewsSample.toPos(wordsAndtagsRef);
		String[] wordsRef = NERNewsSample.toWord(charactersRef, wordsAndtagsRef);
		String[] tagsPre = NERNewsSample.toPos(wordsAndtagsPre);
		String[] wordsPre = NERNewsSample.toWord(charactersRef, wordsAndtagsPre);
		
		NERNewsSample prediction = new NERNewsSample(charactersRef, posRef, wordsAndtagsPre);
		measure.update(wordsRef, tagsRef, wordsPre, tagsPre);

//		if(!sample.equals(prediction)){
//			System.out.println("正确的结果：");
//			for (int i = 0; i < tagsRef.length; i++) {
//				System.out.print(wordsRef[i]+"/"+tagsRef[i]);
//			}
//			System.out.println();
//
//			System.out.println("错误的结果：");
//			for (int i = 0; i < tagsPre.length; i++) {
//				System.out.print(wordsPre[i]+"/"+tagsPre[i]);
//			}
//			System.out.println();
//		}

		return prediction;
	}
}
