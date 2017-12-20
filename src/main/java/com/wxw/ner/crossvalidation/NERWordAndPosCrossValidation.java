package com.wxw.ner.crossvalidation;

import java.io.IOException;

import com.wxw.ner.evaluate.NERMeasure;
import com.wxw.ner.evaluate.NEREvaluateMonitor;
import com.wxw.ner.evaluate.NERWordAndPosEvaluator;
import com.wxw.ner.sample.NERWordOrCharacterSample;
import com.wxw.ner.wordandpos.feature.NERWordAndPosContextGenerator;
import com.wxw.ner.wordandpos.model.NERWordAndPosME;
import com.wxw.ner.wordandpos.model.NERWordAndPosModel;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

/**
 * 基于词性标注的命名实体识别的交叉验证
 * @author 王馨苇
 *
 */
public class NERWordAndPosCrossValidation {

	private final String languageCode;
	private final TrainingParameters params;
	private NEREvaluateMonitor[] monitor;
	
	/**
	 * 构造
	 * @param languageCode 编码格式
	 * @param params 训练的参数
	 * @param listeners 监听器
	 */
	public NERWordAndPosCrossValidation(String languageCode,TrainingParameters params,NEREvaluateMonitor... monitor){
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
			NERWordAndPosContextGenerator contextGenerator) throws IOException{
		CrossValidationPartitioner<NERWordOrCharacterSample> partitioner = new CrossValidationPartitioner<NERWordOrCharacterSample>(sample, nFolds);
		
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<NERWordOrCharacterSample> trainingSampleStream = partitioner.next();

			NERWordAndPosModel model = NERWordAndPosME.train(languageCode, trainingSampleStream, params, contextGenerator);

			NERWordAndPosEvaluator evaluator = new NERWordAndPosEvaluator(new NERWordAndPosME(model, contextGenerator), monitor);
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
