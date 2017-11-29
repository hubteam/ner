package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERWordSample;

import opennlp.tools.util.eval.EvaluationMonitor;

public class NERWordEvaluateMonitor implements EvaluationMonitor<NERWordSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	@Override
	public void correctlyClassified(NERWordSample arg0, NERWordSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	@Override
	public void missclassified(NERWordSample arg0, NERWordSample arg1) {
		// TODO Auto-generated method stub
		
	}
}
