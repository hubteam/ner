package com.wxw.deprun;

import java.io.File;
import java.io.IOException;

import com.wxw.evaluate.NEREvaluateMonitor;
import com.wxw.evaluate.NERMeasure;
import com.wxw.sample.FileInputStreamFactory;
import com.wxw.sample.NERWordAndPosSampleStream;
import com.wxw.sample.NERWordOrCharacterSample;
import com.wxw.wordandpos.NERWordAndPosContextGenerator;
import com.wxw.wordandpos.NERWordAndPosContextGeneratorConfExtend;
import com.wxw.wordandpos.NERWordAndPosEvaluator;
import com.wxw.wordandpos.NERWordAndPosME;
import com.wxw.wordandpos.NERWordAndPosModel;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;

public class NERWordAndPosCrossValidationRun {

	private final String languageCode;

    private final TrainingParameters params;

    private NEREvaluateMonitor[] listeners;
    
    public NERWordAndPosCrossValidationRun(String languageCode,TrainingParameters trainParam,NEREvaluateMonitor... listeners){
    	this.languageCode = languageCode;
        this.params = trainParam;
        this.listeners = listeners;
    }
    
    public void evaluate(ObjectStream<NERWordOrCharacterSample> samples, int nFolds, NERWordAndPosContextGenerator contextGen) throws IOException{
    	CrossValidationPartitioner<NERWordOrCharacterSample> partitioner = new CrossValidationPartitioner<NERWordOrCharacterSample>(samples, nFolds);
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<NERWordOrCharacterSample> trainingSampleStream = partitioner.next();			
			//训练模型
			trainingSampleStream.reset();
			NERWordAndPosModel model = NERWordAndPosME.train(languageCode, trainingSampleStream, params, contextGen);
			NERWordAndPosEvaluator evaluator = new NERWordAndPosEvaluator(new NERWordAndPosME(model, contextGen), listeners);
			NERMeasure measure = new NERMeasure();
			evaluator.setMeasure(measure);
	        //设置测试集（在测试集上进行评价）
	        evaluator.evaluate(trainingSampleStream.getTestSampleStream());
	        System.out.println(measure);
	        run++;
		}
    }
    
    private static void usage(){
    	System.out.println(NERWordAndPosCrossValidationRun.class.getName() + " -data <corpusFile> -encoding <encoding> " + "[-cutoff <num>] [-iters <num>] [-folds <nFolds>] ");
    }
    
    public static void main(String[] args) throws IOException {
    	if (args.length < 1)
        {
            usage();
            return;
        }

        int cutoff = 3;
        int iters = 100;
        int folds = 10;
        File corpusFile = null;
        String encoding = "UTF-8";
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-data"))
            {
                corpusFile = new File(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-encoding"))
            {
                encoding = args[i + 1];
                i++;
            }
            else if (args[i].equals("-cutoff"))
            {
                cutoff = Integer.parseInt(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-iters"))
            {
                iters = Integer.parseInt(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-folds"))
            {
                folds = Integer.parseInt(args[i + 1]);
                i++;
            }
        }

        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
        
        NERWordAndPosContextGenerator context = new NERWordAndPosContextGeneratorConfExtend();
        System.out.println(context);
        ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(corpusFile), encoding);       
        ObjectStream<NERWordOrCharacterSample> sampleStream = new NERWordAndPosSampleStream(lineStream);
        NERWordAndPosCrossValidationRun run = new NERWordAndPosCrossValidationRun("zh",params);
        run.evaluate(sampleStream,folds,context);
	}
}
