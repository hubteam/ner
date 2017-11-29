package com.wxw.ner.evaluate;

import com.wxw.ner.sample.NERWordAndPosSample;

import opennlp.tools.util.eval.EvaluationMonitor;

public class NERWordAndPosEvaluateMonitor implements EvaluationMonitor<NERWordAndPosSample>{

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void correctlyClassified(NERWordAndPosSample arg0, NERWordAndPosSample arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 预测正确的时候执行
	 * @param arg0 参考的结果
	 * @param arg1 预测的结果
	 */
	public void missclassified(NERWordAndPosSample arg0, NERWordAndPosSample arg1) {
		// TODO Auto-generated method stub
		
	}

}
