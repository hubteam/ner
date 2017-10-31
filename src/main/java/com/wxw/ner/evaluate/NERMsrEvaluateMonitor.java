package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERMsrSample;

import opennlp.tools.util.eval.EvaluationMonitor;

public class NERMsrEvaluateMonitor implements EvaluationMonitor<NERMsrSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void correctlyClassified(NERMsrSample arg0, NERMsrSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void missclassified(NERMsrSample arg0, NERMsrSample arg1) {
		// TODO Auto-generated method stub
		
	}

}
