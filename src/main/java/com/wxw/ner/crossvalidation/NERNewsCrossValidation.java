package com.wxw.ner.crossvalidation;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.wxw.ner.evaluate.NERMeasure;
import com.wxw.ner.evaluate.NERMsrEvaluateMonitor;
import com.wxw.ner.evaluate.NERMsrEvaluator;
import com.wxw.ner.evaluate.NERNewsEvaluateMonitor;
import com.wxw.ner.evaluate.NERNewsEvaluator;
import com.wxw.ner.msr.feature.NERMsrContextGenerator;
import com.wxw.ner.msr.model.NERMsrME;
import com.wxw.ner.msr.model.NERMsrModel;
import com.wxw.ner.news.feature.NERNewsContextGenerator;
import com.wxw.ner.news.model.NERNewsME;
import com.wxw.ner.news.model.NERNewsModel;
import com.wxw.ner.sample.NERNewsSample;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

public class NERNewsCrossValidation {

	private final String languageCode;
	private final TrainingParameters params;
	private NERNewsEvaluateMonitor[] monitor;
	
	/**
	 * 构造
	 * @param languageCode 编码格式
	 * @param params 训练的参数
	 * @param listeners 监听器
	 */
	public NERNewsCrossValidation(String languageCode,TrainingParameters params,NERNewsEvaluateMonitor... monitor){
		this.languageCode = languageCode;
		this.params = params;
		this.monitor = monitor;
	}
	
	/**
	 * 交叉验证十折评估
	 * @param sample 样本流
	 * @param nFolds 折数
	 * @param contextGenerator 上下文
	 * @throws IOException io异常
	 */
	public void evaluate(ObjectStream<NERNewsSample> sample, int nFolds,
			NERNewsContextGenerator contextGenerator) throws IOException{
		CrossValidationPartitioner<NERNewsSample> partitioner = new CrossValidationPartitioner<NERNewsSample>(sample, nFolds);
		
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<NERNewsSample> trainingSampleStream = partitioner.next();

			NERNewsModel model = NERNewsME.train(languageCode, trainingSampleStream, params, contextGenerator);

			NERNewsEvaluator evaluator = new NERNewsEvaluator(new NERNewsME(model, contextGenerator), monitor);
			NERMeasure measure = new NERMeasure();
			
			evaluator.setMeasure(measure);
	        //设置测试集（在测试集上进行评价）
	        evaluator.evaluate(trainingSampleStream.getTestSampleStream());
	        
	        System.out.println(measure);
	        run++;
		}
//		System.out.println(measure);
	}
}
