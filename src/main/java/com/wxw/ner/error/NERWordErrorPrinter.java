package com.wxw.ner.error;

import java.io.OutputStream;
import java.io.PrintStream;

import com.wxw.ner.evaluate.NERWordEvaluateMonitor;
import com.wxw.ner.sample.NERWordSample;

/**
 * 基于分词的命名实体识别的错误打印
 * @author 王馨苇
 *
 */
public class NERWordErrorPrinter extends NERWordEvaluateMonitor{

private PrintStream errOut;
	
	public NERWordErrorPrinter(OutputStream out){
		errOut = new PrintStream(out);
	}
	
	/**
	 * 样本和预测的不一样的时候进行输出
	 * @param reference 参考的样本
	 * @param predict 预测的结果
	 */
	@Override
	public void missclassified(NERWordSample reference, NERWordSample predict) {
		 errOut.println("样本的结果：");
		 errOut.print(reference.toSample());
		 errOut.println();
		 errOut.println("预测的结果：");
		 errOut.print(predict.toSample());
		 errOut.println();
	}
}
