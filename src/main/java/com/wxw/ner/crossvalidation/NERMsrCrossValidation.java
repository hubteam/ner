package com.wxw.ner.crossvalidation;

import java.io.IOException;

import com.wxw.ner.evaluate.NERMsrEvaluateMonitor;
import com.wxw.ner.evaluate.NERMeasure;
import com.wxw.ner.evaluate.NERMsrEvaluator;
import com.wxw.ner.msr.feature.NERMsrContextGenerator;
import com.wxw.ner.msr.model.NERMsrME;
import com.wxw.ner.msr.model.NERMsrModel;
import com.wxw.ner.sample.NERMsrSample;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

/**
 * 交叉验证
 * @author 王馨苇
 *
 */
public class NERMsrCrossValidation {

	private final String languageCode;
	private final TrainingParameters params;
	private NERMsrEvaluateMonitor[] monitor;
	
	/**
	 * 构造
	 * @param languageCode 编码格式
	 * @param params 训练的参数
	 * @param listeners 监听器
	 */
	public NERMsrCrossValidation(String languageCode,TrainingParameters params,NERMsrEvaluateMonitor... monitor){
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
	public void evaluate(ObjectStream<NERMsrSample> sample, int nFolds,
			NERMsrContextGenerator contextGenerator) throws IOException{
		CrossValidationPartitioner<NERMsrSample> partitioner = new CrossValidationPartitioner<NERMsrSample>(sample, nFolds);
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<NERMsrSample> trainingSampleStream = partitioner.next();
			
			//训练模型
			trainingSampleStream.reset();
			NERMsrModel model = NERMsrME.train(languageCode, trainingSampleStream, params, contextGenerator);

			NERMsrEvaluator evaluator = new NERMsrEvaluator(new NERMsrME(model, contextGenerator), monitor);
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
