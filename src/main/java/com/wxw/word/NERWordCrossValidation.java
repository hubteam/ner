package com.wxw.word;

import java.io.IOException;

import com.wxw.evaluate.NEREvaluateMonitor;
import com.wxw.evaluate.NERMeasure;
import com.wxw.sample.NERWordOrCharacterSample;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

/**
 * 基于分词的命名实体识别的交叉验证
 * @author 王馨苇
 *
 */
public class NERWordCrossValidation {
	private final String languageCode;
	private final TrainingParameters params;
	private NEREvaluateMonitor[] monitor;
	
	/**
	 * 构造
	 * @param languageCode 编码格式
	 * @param params 训练的参数
	 * @param listeners 监听器
	 */
	public NERWordCrossValidation(String languageCode,TrainingParameters params,NEREvaluateMonitor... monitor){
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
	public void evaluate(ObjectStream<NERWordOrCharacterSample> sample, int nFolds,
			NERWordContextGenerator contextGenerator) throws IOException{
		CrossValidationPartitioner<NERWordOrCharacterSample> partitioner = new CrossValidationPartitioner<NERWordOrCharacterSample>(sample, nFolds);
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<NERWordOrCharacterSample> trainingSampleStream = partitioner.next();
			
			//训练模型
			trainingSampleStream.reset();
			NERWordModel model = NERWordME.train(languageCode, trainingSampleStream, params, contextGenerator);

			NERWordEvaluator evaluator = new NERWordEvaluator(new NERWordME(model, contextGenerator), monitor);
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
