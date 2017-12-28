package com.wxw.wordandpos.run;

import java.io.File;
import java.io.IOException;

import com.wxw.wordandpos.ner.NERWordAndPosContextGenerator;
import com.wxw.wordandpos.ner.NERWordAndPosContextGeneratorConfExtend;
import com.wxw.wordandpos.ner.NERWordAndPosME;

import opennlp.tools.util.TrainingParameters;

public class NERWordAndPosTrainRun {

	private static void usage(){
		System.out.println(NERWordAndPosTrainRun.class.getName()+"-data <corpusFile> -model <modelFile> -encoding"+"[-cutoff <num>] [-iters <num>]");
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length < 1){
			usage();
			return;
		}
		int cutoff = 3;
		int iters = 100;
        File corpusFile = null;
        File modelFile = null;
        String encoding = "UTF-8";
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-data"))
            {
                corpusFile = new File(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-model"))
            {
                modelFile = new File(args[i + 1]);
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
        
        NERWordAndPosContextGenerator contextGen = new NERWordAndPosContextGeneratorConfExtend();
        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
        
        NERWordAndPosME.train(corpusFile, modelFile, params, contextGen, encoding);
	}
}
