package com.wxw.ner.evaluate;

import com.wxw.ner.sample.AbstractNERSample;
import com.wxw.ner.sample.NERWordAndPosSample;
import com.wxw.ner.wordandpos.model.NERWordAndPosME;

import opennlp.tools.util.eval.Evaluator;

/**
 * 基于词性标注的命名实体分析评估器
 * @author 王馨苇
 *
 */
public class NERWordAndPosEvaluator extends Evaluator<AbstractNERSample>{

	private NERWordAndPosME tagger;
	private NERMeasure measure;
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 */
	public NERWordAndPosEvaluator(NERWordAndPosME tagger) {
		this.tagger = tagger;
	}
	
	/**
	 * 构造
	 * @param tagger 训练得到的模型
	 * @param evaluateMonitors 评估的监控管理器
	 */
	public NERWordAndPosEvaluator(NERWordAndPosME tagger,NEREvaluateMonitor... evaluateMonitors) {
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
	protected NERWordAndPosSample processSample(AbstractNERSample sample) {
		String[] charactersRef = sample.getWords();
		String[] posRef = sample.getPoses();
		String[] wordsAndtagsRef = sample.getTags();
		String[][] acRef = sample.getAditionalContext();
		
		String[] wordsAndtagsPre = tagger.tag(charactersRef, posRef, acRef);
		
		String[] tagsRef = AbstractNERSample.toNer(wordsAndtagsRef);
		String[] wordsRef = AbstractNERSample.toWord(charactersRef, wordsAndtagsRef);
		String[] tagsPre = AbstractNERSample.toNer(wordsAndtagsPre);
		String[] wordsPre = AbstractNERSample.toWord(charactersRef, wordsAndtagsPre);
		
		NERWordAndPosSample prediction = new NERWordAndPosSample(charactersRef, posRef, wordsAndtagsPre);
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
