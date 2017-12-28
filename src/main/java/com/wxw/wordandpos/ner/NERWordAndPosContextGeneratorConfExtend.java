package com.wxw.wordandpos.ner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.wxw.ner.dict.ReadAdditionalDitionary;

public class NERWordAndPosContextGeneratorConfExtend implements NERWordAndPosContextGenerator{

	private boolean w_2Set;
    private boolean w_1Set;
    private boolean w0Set;
    private boolean w1Set;
    private boolean w2Set;
    private boolean p_2Set;
    private boolean p_1Set;
    private boolean p0Set;
    private boolean p1Set;
    private boolean p2Set;
    private boolean t_1Set;
    private boolean t_2Set;
    
    private boolean p_2p0Set;
    private boolean p_2p1Set;
    private boolean p_1p0Set;
    private boolean w1p0Set;
    private boolean p0p1Set;
    private boolean p0p2Set;
    private boolean w_1p0Set;
    private boolean w_1p_1Set;
    private boolean w1p1Set;
    private boolean w2p2Set;
    private boolean p0t_1Set;
    private boolean p0t_2Set;
    private boolean w0t_2Set;
    private boolean w0t_1Set;
    private boolean t_1t_2p0Set;
    private boolean t_2p0p1Set;
    private boolean p_1t_1p0Set;
    private boolean t_1w0p0Set;
    
  //增加的字典特征的控制变量
    private boolean Lt0Set;
    private boolean w_1t0Set;
    private boolean w0t0Set;
    private boolean w1t0Set;
    
    Set<String> dictionalWords = ReadAdditionalDitionary.getWords("E:\\jigouming.txt", "utf8");
	/**
	 * 构造
	 * @throws IOException
	 */
	public NERWordAndPosContextGeneratorConfExtend() throws IOException{
		Properties featureConf = new Properties();
        InputStream featureStream = NERWordAndPosContextGeneratorConfExtend.class.getClassLoader().getResourceAsStream("com/wxw/ner/run/feature.properties");
        featureConf.load(featureStream);
        
        init(featureConf);
	}
	
	/**
	 * 构造
	 * @param properties 配置文件
	 */
	public NERWordAndPosContextGeneratorConfExtend(Properties properties){
        
        init(properties);
	}
	
	/**
	 * 根据配置文件的信息初始化特征
	 * @param config 配置文件
	 */
	private void init(Properties config) {
		w_2Set = (config.getProperty("wordposextend.w_2", "true").equals("true"));
        w_1Set = (config.getProperty("wordposextend.w_1", "true").equals("true"));
        w0Set = (config.getProperty("wordposextend.w0", "true").equals("true"));
        w1Set = (config.getProperty("wordposextend.w1", "true").equals("true"));
        w2Set = (config.getProperty("wordposextend.w2", "true").equals("true"));
        
        p_2Set = (config.getProperty("wordposextend.p_2", "true").equals("true"));
        p_1Set = (config.getProperty("wordposextend.p_1", "true").equals("true"));
        p0Set = (config.getProperty("wordposextend.p0", "true").equals("true"));
        p1Set = (config.getProperty("wordposextend.p1", "true").equals("true"));
        p2Set = (config.getProperty("wordposextend.p2", "true").equals("true"));

        t_1Set = (config.getProperty("wordposextend.t_1", "true").equals("true"));
        t_2Set = (config.getProperty("wordposextend.t_2", "true").equals("true"));
        
        p_2p0Set = (config.getProperty("wordposextend.p_2p0", "true").equals("true"));
        p_2p1Set = (config.getProperty("wordposextend.p_2p1", "true").equals("true"));
        p_1p0Set = (config.getProperty("wordposextend.p_1p0", "true").equals("true"));
        w1p0Set = (config.getProperty("wordposextend.w1p0", "true").equals("true"));
        p0p1Set = (config.getProperty("wordposextend.p0p1", "true").equals("true"));
        p0p2Set = (config.getProperty("wordposextend.p0p2", "true").equals("true"));
        
        w_1p0Set = (config.getProperty("wordposextend.w_1p0", "true").equals("true"));
        w_1p_1Set = (config.getProperty("wordposextend.w_1p_1", "true").equals("true"));
        w1p1Set = (config.getProperty("wordposextend.w1p1", "true").equals("true"));
        w2p2Set = (config.getProperty("wordposextend.w2p2", "true").equals("true"));
        
        p0t_1Set = (config.getProperty("wordposextend.p0t_1", "true").equals("true"));
        p0t_2Set = (config.getProperty("wordposextend.p0t_2", "true").equals("true"));
        
        w0t_2Set = (config.getProperty("wordposextend.w0t_2", "true").equals("true"));
        w0t_1Set = (config.getProperty("wordposextend.w0t_1", "true").equals("true"));
        
        t_1t_2p0Set = (config.getProperty("wordposextend.t_1t_2p0", "true").equals("true"));
        t_2p0p1Set = (config.getProperty("wordposextend.t_2p0p1", "true").equals("true"));
        p_1t_1p0Set = (config.getProperty("wordposextend.p_1t_1p0", "true").equals("true"));
        t_1w0p0Set = (config.getProperty("wordposextend.t_1w0p0", "true").equals("true"));
        
     // 获取配置文件中的字典特征的设置值
        Lt0Set = (config.getProperty("wordposextend.Lt0", "true").equals("true"));
        w_1t0Set = (config.getProperty("wordposextend.w_1t0", "true").equals("true"));
        w0t0Set = (config.getProperty("wordposextend.w0t0", "true").equals("true"));
        w1t0Set = (config.getProperty("wordposextend.w1t0", "true").equals("true"));      
	}
	
	public String[] getContext(int index, String[] words, String[] poses, String[] tags, Object[] ac) {
		// TODO Auto-generated method stub
		return getContext(index,words,poses,tags);
	}
	
	/**
	 * 判断当前的词语是否是词典中的词
	 * @param dictionaryWords 词典
	 * @param words 当前要判断的词语
	 * @return
	 */
	public boolean isDictionalWords(String words){
		
		if(dictionalWords.contains(words)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 得到特征
	 * @param index 当前位置
	 * @param words 词语数组
	 * @param poses 词性数组
	 * @param tags 命名实体标记数组
	 * @return
	 */
	public String[] getContext(int index, String[] words, String[] poses, String[] tags) {
		String w1, w2, w3, w4,w5,w6,w7, w0, w_1, w_2,w_3,w_4,w_5,w_6,w_7;
        w1 = w2 = w3 = w4 = w5 = w6 = w7 = w0 = w_1 = w_2 = w_3 = w_4 = w_5 = w_6 = w_7 = null;
        String p1, p2, p0, p_1, p_2;
        p1 = p2 = p0 = p_1 = p_2 = null;
        String t_1 = null;
        String t_2 = null;
        
        w0 = words[index];
        p0 = poses[index];
        if (words.length > index + 1) {
            w1 = words[index + 1];
            p1 = poses[index + 1];
            if (words.length > index + 2) {
                w2 = words[index + 2];
                p2 = poses[index + 2];
                if(words.length > index + 3){
                	w3 = words[index + 3];
                	if(words.length > index + 4){
                		w4 = words[index + 4];
                		if(words.length > index + 5){
                			w5 = words[index + 5];
                			if(words.length > index + 6){
                				w6 = words[index + 6];
                				if(words.length > index + 7){
                					w7 = words[index + 7];
                				}
                			}
                		}
                	}
                }
            }
        }

        if (index - 1 >= 0) {
            w_1 = words[index - 1];
            p_1 = poses[index - 1];
            t_1 = tags[index - 1];
            if (index - 2 >= 0) {
                w_2 = words[index - 2];
                p_2 = poses[index - 2];
                t_2 = tags[index - 2];
                if(index -3 >= 0){
                	w_3 = words[index - 3];
                	if(index - 4 >= 0){
                		w_4 = words[index - 4];
                		if(index - 5 >= 0){
                			w_5 = words[index - 5];
                			if(index - 6 >= 0){
                				w_6 = words[index - 6];
                				if(index - 7 >= 0){
                					w_7 = words[index - 7];
                				}
                			}
                		}
                	}
                }
            }
        }

        List<String> features = new ArrayList<String>();
        
        if (w0Set) {
            features.add("w0=" + w0);
        }
        
        if(p0Set){
        	features.add("p0="+p0);
        }

        if (w_1 != null) {
            if (w_1Set) {
                features.add("w_1=" + w_1);
            }

            if (t_1Set) {
                features.add("t_1=" + t_1);
            }
            
            if(p_1Set){
            	features.add("p_1=" + p_1);
            }

            if (w_2 != null) {
                if (w_2Set) {
                    features.add("w_2=" + w_2);
                }

                if (t_2Set) {
                    features.add("t_2=" + t_2);
                }
                
                if (p_2Set) {
                    features.add("p_2=" + p_2);
                }
            }
        }

        if (w1 != null) {
            if (w1Set) {
                features.add("w1=" + w1);
            }
            if (p1Set) {
                features.add("p1=" + p1);
            }
            if (w2 != null) {
                if (w2Set) {
                    features.add("w2=" + w2);
                }
                if (p2Set) {
                    features.add("p2=" + p2);
                }
            }
        }

        if(p_2 != null){
        	if(p_2p0Set){
        		features.add("p_2p0="+p_2+p0);
        	}
        }
        
        if(p1 != null && p_2 != null){
        	if(p_2p1Set){
        		features.add("p_2p1="+p_2+p1);
        	}
        }
        
        if(p_1 != null){
        	if(p_1p0Set){
        		features.add("p_1p0="+p_1+p0);
        	}
        }
        //w1p0Set
        if(w1 != null){
        	if(w1p0Set){
        		features.add("w1p0="+w1+p0);
        	}
        }
        //p0p1Set
        if(p1 != null){
        	if(p0p1Set){
        		features.add("p0p1="+p0+p1);
        	}
        }
        //p0p2Set
        if(p2 != null){
        	if(p0p2Set){
        		features.add("p0p2="+p0+p2);
        	}
        }
        //w_1p0Set
        if(w_1 != null){
        	if(w_1p0Set){
        		features.add("w_1p0="+w_1+p0);
        	}
        }
        //w_1p_1Set
        if(w_1 != null && p_1 != null){
        	if(w_1p_1Set){
        		features.add("w_1p_1="+w_1+p_1);
        	}
        }
        //w1p1Set
        if(w1 != null && p1 != null){
        	if(w1p1Set){
        		features.add("w1p1="+w1+p1);
        	}
        }
        //w2p2Set
        if(w2 != null && p2 != null){
        	if(w2p2Set){
        		features.add("w2p2="+w2+p2);
        	}
        }
        //p0t_1Set
        if(t_1 != null){
        	if(p0t_1Set){
        		features.add("p0t_1="+p0+t_1);
        	}
        }
        //p0t_2Set
        if(t_2 != null){
        	if(p0t_2Set){
        		features.add("p0t_2="+p0+t_2);
        	}
        }
      //w0t_2Set
        if(t_2 != null){
        	if(w0t_2Set){
        		features.add("w0t_2="+w0+t_2);
        	}
        }
      //w0t_1Set
        if(t_1 != null){
        	if(w0t_1Set){
        		features.add("w0t_1="+w0+t_1);
        	}
        }
        //t_1t_2p0Set
        if(t_1 != null && t_2 != null){
        	if(t_1t_2p0Set){
        		features.add("t_1t_2p0="+t_1+t_2+p0);
        	}
        }
        //t_2p0p1Set
        if(p1 != null && t_2 != null){
        	if(t_2p0p1Set){
        		features.add("t_2p0p1="+t_2+p0+p1);
        	}
        }
        //p_1t_1p0Set
        if(p_1 != null && t_1 != null){
        	if(p_1t_1p0Set){
        		features.add("p_1t_1p0="+p_1+t_1+p0);
        	}
        }
        //t_1w0p0Set
        if(t_1 != null){
        	if(t_1w0p0Set){
        		features.add("t_1w0p0="+t_1+w0+p0);
        	}
        }
        //匹配词典中词的特征
        //人名词典
//        boolean flagByTwo = true;
//        // (1)w_1w0
//        if (w_1 != null && w0 != null){
//        	
//            if (isDictionalWords( w_1 + w0))
//            {
//                if (Lt0Set)
//                {
//                    features.add("Le=" + 2);
//                }
//                if(w_1 != null){
//                	if (w_1t0Set)
//                    {
//                        features.add("w_1e=" + w_1);
//                    }
//                }
//                
//                if (w0t0Set)
//                {
//                    features.add("w0e=" + w0);
//                }
//                if(w1 != null){
//                	 if (w1t0Set)
//                     {
//                         features.add("w1e=" + w1);
//                     }
//                }
//                flagByTwo = false;
//            }
//        }
//        
//        // (2)w0w1
//        if (w0 != null && w1 != null)
//        {
//            if (isDictionalWords(w0 + w1))
//            {
//            	 if (Lt0Set)
//                 {
//                     features.add("Lb=" + 2);
//                 }
//                 if(w_1 != null){
//                 	if (w_1t0Set)
//                     {
//                         features.add("w_1b=" + w_1);
//                     }
//                 }
//                 
//                 if (w0t0Set)
//                 {
//                     features.add("w0b=" + w0);
//                 }
//                 if(w1 != null){
//                 	if (w1t0Set)
//                     {
//                         features.add("w1b=" + w1);
//                     }
//                 }
//                flagByTwo = false;
//            }
//        }
//        // (3)w0
//        if (w0 != null && flagByTwo)
//        {
//            if (isDictionalWords(w0))
//            {
//            	if (Lt0Set)
//                {
//                    features.add("Ls=" + 1);
//                }
//            	if(w_1 != null){
//            		if (w_1t0Set)
//                    {
//                        features.add("w_1s=" + w_1);
//                    }
//            	}
//                if (w0t0Set)
//                {
//                    features.add("w0s=" + w0);
//                }
//                if(w1 != null){
//                	if (w1t0Set)
//                    {
//                        features.add("w1s=" + w1);
//                    }
//                }
//            }
//        }
        
      //地名,机构名词典
        boolean flagBySeven = true;
        boolean flagBySix = true;
        boolean flagByFive = true;
        boolean flagByFour = true;
        boolean flagByThree = true;
        boolean flagByTwo = true;
        boolean flagByOne = true;
        
        // (1)w_7w_6w_5w_4w_3w_2w_1w0
        if (w_7 != null && w_6 != null && w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null){
        	
            if (isDictionalWords( w_7+w_6+w_5+w_4+w_3+w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_6w_5w_4w_3w_2w_1w0w1
        if (w_6 != null && w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null){
        	
            if (isDictionalWords(w_6+w_5+w_4+w_3+w_2+w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_5w_4w_3w_2w_1w0w1w2
        if (w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null){
        	
            if (isDictionalWords(w_5+w_4+w_3+w_2+w_1 + w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_4w_3w_2w_1w0w1w2w3
        if (w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null){
        	
            if (isDictionalWords(w_4+w_3+w_2+w_1 + w0+w1+w2+w3))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // w_3w_2w_1w0w1w2w3w4
        if (w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null){
        	
            if (isDictionalWords(w_3+w_2+w_1 + w0+w1+w2+w3 + w4))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_2w_1w0w1w2w3w4w5
        if (w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null){
        	
            if (isDictionalWords(w_2+w_1 + w0+w1+w2+w3 + w4+w5))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_1w0w1w2w3w4w5w6
        if (w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null && w6 != null){
        	
            if (isDictionalWords(w_1 + w0+w1+w2+w3 + w4+w5+w6))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w0w1w2w3w4w5w6w7
        if (w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null && w6 != null && w7 != null){
        	
            if (isDictionalWords(w0+w1+w2+w3 + w4+w5+w6+w7))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 8);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagBySeven = false;
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_6w_5w_4w_3w_2w_1w0
        if (w_6 != null && w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && flagBySeven){
        	
            if (isDictionalWords(w_6+w_5+w_4+w_3+w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        // w_5w_4w_3w_2w_1w0w1
        if (w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && flagBySeven){
        	
            if (isDictionalWords(w_5+w_4+w_3+w_2+w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        // w_4w_3w_2w_1w0w1w2
        if (w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && flagBySeven){
        	
            if (isDictionalWords(w_4+w_3+w_2+w_1 + w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_3w_2w_1w0w1w2w3
        if (w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && flagBySeven){
        	
            if (isDictionalWords(w_3+w_2+w_1 + w0+w1+w2+w3))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_2w_1w0w1w2w3w4
        if (w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && flagBySeven){
        	
            if (isDictionalWords(w_2+w_1 + w0+w1+w2+w3+w4))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // w_1w0w1w2w3w4w5
        if (w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null && flagBySeven){
        	
            if (isDictionalWords(w_1 + w0+w1+w2+w3+w4+w5))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // w0w1w2w3w4w5w6
        if (w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null && w6 != null && flagBySeven){
        	
            if (isDictionalWords(w0+w1+w2+w3+w4+w5+w6))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 7);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagBySix = false;
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_5w_4w_3w_2w_1w0
        if (w_5 != null && w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && flagBySix){
        	
            if (isDictionalWords(w_5+w_4+w_3+w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_4w_3w_2w_1w0w1
        if (w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && flagBySix){
        	
            if (isDictionalWords(w_4+w_3+w_2+w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_3w_2w_1w0w1w2
        if (w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && flagBySix){
        	
            if (isDictionalWords(w_3+w_2+w_1 + w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_2w_1w0w1w2w3
        if (w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && flagBySix){
        	
            if (isDictionalWords(w_2+w_1 + w0+w1+w2+w3))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_1w0w1w2w3w4
        if (w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && flagBySix){
        	
            if (isDictionalWords(w_1 + w0+w1+w2+w3+w4))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w0w1w2w3w4w5
        if (w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && w5 != null && flagBySix){
        	
            if (isDictionalWords(w0+w1+w2+w3+w4+w5))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 6);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagByFive = false;
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_4w_3w_2w_1w0
        if (w_4 != null && w_3 != null && w_2 != null && w_1 != null && w0 != null && flagByFive){
        	
            if (isDictionalWords(w_4+w_3+w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 5);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        // (1)w_3w_2w_1w0w1
        if (w_3 != null && w_2 != null && w_1 != null && w0 != null && w1 != null && flagByFive){
        	
            if (isDictionalWords(w_3+w_2+w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 5);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_2w_1w0w1w2
        if (w_2 != null && w_1 != null && w0 != null && w1 != null && w2 != null && flagByFive){
        	
            if (isDictionalWords(w_2+w_1 + w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 5);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        // (1)w_1w0w1w2w3
        if (w_1 != null && w0 != null && w1 != null && w2 != null && w3 != null && flagByFive){
        	
            if (isDictionalWords(w_1 + w0+w1+w2+w3))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 5);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w0w1w2w3w4
        if (w0 != null && w1 != null && w2 != null && w3 != null && w4 != null && flagByFive){
        	
            if (isDictionalWords(w0+w1+w2+w3+w4))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 5);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagByFour = false;
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_3w_2w_1w0
        if (w_3 != null && w_2 != null && w_1 != null && w0 != null && flagByFour){
        	
            if (isDictionalWords(w_3+w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 4);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_2w_1w0w1
        if (w_2 != null && w_1 != null && w0 != null && w1 != null && flagByFour){
        	
            if (isDictionalWords(w_2+w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 4);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_1w0w1w2
        if (w_1 != null && w0 != null && w1 != null && w2 != null && flagByFour){
        	
            if (isDictionalWords(w_1 + w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 4);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w0w1w2w3
        if (w0 != null && w1 != null && w2 != null && w3 != null && flagByFour){
        	
            if (isDictionalWords(w0+w1+w2+w3))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 4);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagByThree = false;
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_2w_1w0
        if (w_2 != null && w_1 != null && w0 != null && flagByThree){
        	
            if (isDictionalWords(w_2+w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 3);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagByTwo = false;
                flagByOne = false;
            }
        }
     // (1)w_1w0w1
        if (w_1 != null && w0 != null && w1 != null && flagByThree){
        	
            if (isDictionalWords(w_1 + w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lm=" + 3);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1m=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0m=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1m=" + w1);
                     }
                }
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w0w1w2
        if (w0 != null && w1 != null && w2 != null && flagByThree){
        	
            if (isDictionalWords(w0+w1+w2))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 3);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagByTwo = false;
                flagByOne = false;
            }
        }
        
     // (1)w_1w0
        if (w_1 != null && w0 != null && flagByTwo){
        	
            if (isDictionalWords(w_1 + w0))
            {
                if (Lt0Set)
                {
                    features.add("Le=" + 2);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1e=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0e=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1e=" + w1);
                     }
                }
                flagByOne = false;
            }
        }
        //(1)w0w1
        if (w0 != null && w1 != null && flagByTwo){
        	
            if (isDictionalWords(w0+w1))
            {
                if (Lt0Set)
                {
                    features.add("Lb=" + 2);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1b=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0b=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1b=" + w1);
                     }
                }
                flagByOne = false;
            }
        }
      //(1)w0
        if (w0 != null && flagByOne){
        	
            if (isDictionalWords(w0))
            {
                if (Lt0Set)
                {
                    features.add("Ls=" + 1);
                }
                if(w_1 != null){
                	if (w_1t0Set)
                    {
                        features.add("w_1s=" + w_1);
                    }
                }
                
                if (w0t0Set)
                {
                    features.add("w0s=" + w0);
                }
                if(w1 != null){
                	 if (w1t0Set)
                     {
                         features.add("w1s=" + w1);
                     }
                }
            }
        }
        String[] contexts = features.toArray(new String[features.size()]);

        return contexts;
	}

	@Override
	public String toString() {
		/**
		 * private boolean w_2Set;
    private boolean w_1Set;
    private boolean w0Set;
    private boolean w1Set;
    private boolean w2Set;
    private boolean p_2Set;
    private boolean p_1Set;
    private boolean p0Set;
    private boolean p1Set;
    private boolean ;
    private boolean ;
    private boolean ;
		 */
		return "NERWordAndPosContextGeneratorConfExtend{" + "w_2Set=" + w_2Set + ", w_1Set=" + w_1Set + 
                ", w0Set=" + w0Set + ", w1Set=" + w1Set + ", w2Set=" + w2Set + 
                ", p_2Set=" + p_2Set + ", p_1Set=" + p_1Set + 
                ", p0Set=" + p0Set + ", p1Set=" + p1Set + ", p2Set=" + p2Set + 
                ", t_1Set=" + t_1Set + ", t_2Set=" + t_2Set + 
                ", p_2p0Set=" + p_2p0Set + ", p_2p1Set=" + p_2p1Set + ", p_1p0Set=" + p_1p0Set + 
                ", w1p0Set=" + w1p0Set + ", p0p1Set=" + p0p1Set + ", p0p2Set=" + p0p2Set +                
                ", w_1p0Set=" + w_1p0Set + ", w_1p_1Set=" + w_1p_1Set + ", w1p1Set=" + w1p1Set + 
                ", w2p2Set=" + w2p2Set + ", p0t_1Set=" + p0t_1Set + 
                ", p0t_2Set=" + p0t_2Set + ", w0t_2Set=" + w0t_2Set + ", w0t_1Set=" + w0t_1Set + 
                ", t_1t_2p0Set=" + t_1t_2p0Set + ", t_2p0p1Set=" + t_2p0p1Set + ", p_1t_1p0Set=" + p_1t_1p0Set + 
                ", t_1w0p0Set=" + t_1w0p0Set + 
                 ", Lt0Set=" + Lt0Set + ", w_1t0Set=" + w_1t0Set + ", w0t0Set=" + w0t0Set + ", w1t0Set=" + w1t0Set + 
                '}';
	}
}

