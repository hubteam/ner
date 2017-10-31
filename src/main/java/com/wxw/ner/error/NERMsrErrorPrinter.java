package com.wxw.ner.error;

import java.io.OutputStream;
import java.io.PrintStream;

import com.wxw.ner.evaluate.NERMsrEvaluateMonitor;
import com.wxw.ner.sample.NERMsrSample;

/**
 * 打印错误信息类 
 * @author 王馨苇
 *
 */
public class NERMsrErrorPrinter extends NERMsrEvaluateMonitor{

private PrintStream errOut;
	
	public NERMsrErrorPrinter(OutputStream out){
		errOut = new PrintStream(out);
	}
	
	/**
	 * 样本和预测的不一样的时候进行输出
	 * @param reference 参考的样本
	 * @param predict 预测的结果
	 */
	@Override
	public void missclassified(NERMsrSample reference, NERMsrSample predict) {
		 errOut.println("样本的结果：");
		 errOut.print(reference.toSample());
		 errOut.println();
		 errOut.println("预测的结果：");
		 errOut.print(predict.toSample());
		 errOut.println();
	}
}
