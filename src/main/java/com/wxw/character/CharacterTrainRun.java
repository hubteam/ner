package com.wxw.character;

import java.io.File;
import java.io.IOException;

import com.wxw.ner.character.feature.NERCharacterContextGenerator;
import com.wxw.ner.character.feature.NERCharacterContextGeneratorConf;
import com.wxw.ner.character.model.NERCharacterME;
import com.wxw.word.feature.NERWordContextGenerator;
import com.wxw.word.feature.NERWordContextGeneratorConf;
import com.wxw.word.model.NERWordME;

import opennlp.tools.util.TrainingParameters;

public class CharacterTrainRun {
	private static void usage(){
		System.out.println(CharacterTrainRun.class.getName()+"-data <corpusFile> -model <modelFile> -encoding"+"[-cutoff <num>] [-iters <num>]");
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
        
        NERCharacterContextGenerator contextGen = new NERCharacterContextGeneratorConf();
        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
        
        NERCharacterME.train(corpusFile, modelFile, params, contextGen, encoding);
	}
}
