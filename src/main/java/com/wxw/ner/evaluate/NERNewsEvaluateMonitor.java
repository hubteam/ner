package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERMsrSample;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.util.eval.EvaluationMonitor;

public class NERNewsEvaluateMonitor implements EvaluationMonitor<NERNewsSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void correctlyClassified(NERNewsSample arg0, NERNewsSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void missclassified(NERNewsSample arg0, NERNewsSample arg1) {
		// TODO Auto-generated method stub
		
	}

}
