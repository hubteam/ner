package com.wxw.character.run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wxw.ner.character.feature.NERCharacterContextGenerator;
import com.wxw.ner.character.feature.NERCharacterContextGeneratorConf;
import com.wxw.ner.character.model.NERCharacterME;
import com.wxw.ner.character.model.NERCharacterModel;
import com.wxw.ner.error.NERErrorPrinter;
import com.wxw.ner.evaluate.NERCharacterEvaluator;
import com.wxw.ner.evaluate.NERMeasure;
import com.wxw.ner.sample.NERWordOrCharacterSample;
import com.wxw.ner.sample.FileInputStreamFactory;
import com.wxw.ner.sample.NERCharacterSampleStream;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class NERCharacterEvaluateRun {
	private static void usage(){
		System.out.println(NERCharacterEvaluateRun.class.getName() + " -data <trainFile> -gold <goldFile> -error <errorFile> -encoding <encoding>" + " [-cutoff <num>] [-iters <num>]");
	}
	
	public static void eval(File trainFile, TrainingParameters params, File goldFile, String encoding, File errorFile) throws IOException{
		long start = System.currentTimeMillis();
        NERCharacterContextGenerator contextGen = new NERCharacterContextGeneratorConf();
        NERCharacterModel model = NERCharacterME.train(trainFile, params, contextGen, encoding);
        System.out.println("训练时间： " + (System.currentTimeMillis() - start));
        NERCharacterME tagger = new NERCharacterME(model,contextGen);
        
        NERMeasure measure = new NERMeasure();
        NERCharacterEvaluator evaluator = null;
        NERErrorPrinter printer = null;
        if(errorFile != null){
        	System.out.println("Print error to file " + errorFile);
        	printer = new NERErrorPrinter(new FileOutputStream(errorFile));    	
        	evaluator = new NERCharacterEvaluator(tagger,printer);
        }else{
        	evaluator = new NERCharacterEvaluator(tagger);
        }
        evaluator.setMeasure(measure);
        ObjectStream<String> linesStream = new PlainTextByLineStream(new FileInputStreamFactory(goldFile), encoding);
        ObjectStream<NERWordOrCharacterSample> sampleStream = new NERCharacterSampleStream(linesStream);
        evaluator.evaluate(sampleStream);
        NERMeasure measureRes = evaluator.getMeasure();
        System.out.println("标注时间： " + (System.currentTimeMillis() - start));
        System.out.println(measureRes);  
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1){
            usage();
            return;
        }
        String trainFile = null;
        String goldFile = null;
        String errorFile = null;
        String encoding = null;
        int cutoff = 3;
        int iters = 100;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-data"))
            {
                trainFile = args[i + 1];
                i++;
            }
            else if (args[i].equals("-gold"))
            {
                goldFile = args[i + 1];
                i++;
            }
            else if (args[i].equals("-error"))
            {
                errorFile = args[i + 1];
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
        }

        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
        if (errorFile != null)
        {
            eval(new File(trainFile), params, new File(goldFile), encoding, new File(errorFile));
        }
        else
            eval(new File(trainFile), params, new File(goldFile), encoding, null);
	}
		
}
