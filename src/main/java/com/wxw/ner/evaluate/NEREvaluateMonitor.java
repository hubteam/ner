package com.wxw.ner.evaluate;

import com.wxw.ner.sample.AbstractNERSample;
import opennlp.tools.util.eval.EvaluationMonitor;

public class NEREvaluateMonitor implements EvaluationMonitor<AbstractNERSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void correctlyClassified(AbstractNERSample arg0, AbstractNERSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void missclassified(AbstractNERSample arg0, AbstractNERSample arg1) {
		// TODO Auto-generated method stub
		
	}

}
