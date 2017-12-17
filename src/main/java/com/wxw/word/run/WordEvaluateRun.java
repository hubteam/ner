package com.wxw.word.run;

import java.io.File;
import java.io.IOException;

import com.wxw.word.feature.NERWordContextGenerator;
import com.wxw.word.feature.NERWordContextGeneratorConf;
import com.wxw.word.model.NERWordME;
import com.wxw.word.model.NERWordModel;

import opennlp.tools.util.TrainingParameters;

public class WordEvaluateRun {

	private static void usage(){
		System.out.println(WordEvaluateRun.class.getName() + " -data <trainFile> -gold <goldFile> -encoding <encoding> [-error <errorFile>]" + " [-cutoff <num>] [-iters <num>]");
	}
	
	public static void eval(File trainFile, TrainingParameters params, File goldFile, String encoding, File errorFile) throws IOException{
		long start = System.currentTimeMillis();
        NERWordContextGenerator contextGen = new NERWordContextGeneratorConf();
        NERWordModel model = NERWordME.train(trainFile, params, contextGen, encoding);
        System.out.println("训练时间： " + (System.currentTimeMillis() - start));
        
	}
	
	public static void main(String[] args) {
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
	}
		
}
