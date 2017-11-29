package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERCharacterSample;

import opennlp.tools.util.eval.EvaluationMonitor;

public class NERCharacterEvaluateMonitor implements EvaluationMonitor<NERCharacterSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void correctlyClassified(NERCharacterSample arg0, NERCharacterSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void missclassified(NERCharacterSample arg0, NERCharacterSample arg1) {
		// TODO Auto-generated method stub
		
	}

}
