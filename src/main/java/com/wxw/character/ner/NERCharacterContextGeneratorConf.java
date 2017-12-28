package com.wxw.character.ner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.wxw.ner.dict.DictionaryWrapper;
import com.wxw.ner.dict.ReadAdditionalDitionary;
/**
 * 根据配置文件生成特征
 * @author 王馨苇
 *
 */
public class NERCharacterContextGeneratorConf implements NERCharacterContextGenerator{

	private boolean c_2Set;
    private boolean c_1Set;
    private boolean c0Set;
    private boolean c1Set;
    private boolean c2Set;
    private boolean c_2c_1Set;
    private boolean c_1c0Set;
    private boolean c0c1Set;
    private boolean c1c2Set;
    private boolean c_1c1Set;
    private boolean PuSet;
    private boolean TSet;
    private boolean t_1Set;
    private boolean t_2Set;
    private boolean t_2t_1Set;
  //增加的字典特征的控制变量
    private boolean Lt0Set;
    private boolean c_1t0Set;
    private boolean c0t0Set;
    private boolean c1t0Set;
    DictionaryWrapper wrapperNT;
    Set<String> dictionaryWordsNT;
    int lengthNT;
    DictionaryWrapper wrapperNS;
    Set<String> dictionaryWordsNS;
    int lengthNS;
    DictionaryWrapper wrapperNR;
    Set<String> dictionaryWordsNR;
    int lengthNR;
	/**
	 * 构造
	 * @throws IOException
	 */
	public NERCharacterContextGeneratorConf() throws IOException{
		Properties featureConf = new Properties();
        InputStream featureStream = NERCharacterContextGeneratorConf.class.getClassLoader().getResourceAsStream("com/wxw/ner/run/feature.properties");
        featureConf.load(featureStream);
        
        init(featureConf);
	}
	
	public NERCharacterContextGeneratorConf(Properties properties){
//		wrapperNT = ReadAdditionalDitionary.getLengthAndWords("E:\\jigouming.txt", "utf8");
//		dictionaryWordsNT = wrapperNT.getWordSet();
//	    lengthNT = wrapperNT.getLength();
//	    wrapperNR = ReadAdditionalDitionary.getLengthAndWords("E:\\renming.txt", "utf8");
//		dictionaryWordsNR = wrapperNR.getWordSet();
//		for (String string : dictionaryWordsNR) {
//			System.out.println(string);
//		}
//	    lengthNR = wrapperNR.getLength();
//	    System.out.println(lengthNR);
//	    wrapperNS = ReadAdditionalDitionary.getLengthAndWords("E:\\diming.txt", "utf8");
//		dictionaryWordsNS = wrapperNS.getWordSet();
//	    lengthNS = wrapperNS.getLength();
//	    System.out.println(lengthNS);
        init(properties);
	}
	
	/**
     * 初始化配置文件
     * @param featureConf 配置文件
     */
	private void init(Properties config) {
		c_2Set = (config.getProperty("character.c_2", "true").equals("true"));
        c_1Set = (config.getProperty("character.c_1", "true").equals("true"));
        c0Set = (config.getProperty("character.c0", "true").equals("true"));
        c1Set = (config.getProperty("character.c1", "true").equals("true"));
        c2Set = (config.getProperty("character.c2", "true").equals("true"));

        c_2c_1Set = (config.getProperty("character.c_2c_1", "true").equals("true"));
        c_1c0Set = (config.getProperty("character.c_1c0", "true").equals("true"));
        c0c1Set = (config.getProperty("character.c0c1", "true").equals("true"));
        c1c2Set = (config.getProperty("character.c1c2", "true").equals("true"));

        c_1c1Set = (config.getProperty("character.c_1c1", "true").equals("true"));
        
        PuSet = (config.getProperty("character.Pu", "true").equals("true"));
        
        TSet = (config.getProperty("character.T", "true").equals("true"));
        
        t_1Set = (config.getProperty("character.t_1", "true").equals("true"));
        t_2Set = (config.getProperty("character.t_2", "true").equals("true"));
        t_2t_1Set = (config.getProperty("character.t_2t_1", "true").equals("true"));  
        
     // 获取配置文件中的字典特征的设置值
        Lt0Set = (config.getProperty("character.Lt0", "true").equals("true"));
        c_1t0Set = (config.getProperty("character.c_1t0", "true").equals("true"));
        c0t0Set = (config.getProperty("character.c0t0", "true").equals("true"));
        c1t0Set = (config.getProperty("character.c1t0", "true").equals("true"));
	}
	
	public String[] getContext(int index, String[] words, String[] tags, Object[] ac) {
		
		return getContext(index,words,tags);
	}

	private String[] getContext(int index, String[] words, String[] tags) {
		String w1, w2, w3,w4,w5,w6, w0, w_1, w_2,w_3,w_4,w_5,w_6;
        w1 = w2 = w3 = w4 = w5 = w6 = w0 = w_1 = w_2 = w_3= w_4 = w_5 = w_6 =  null;
        String TC_1, TC_2, TC0, TC1, TC2;
        TC_1 = TC_2 = TC0 = TC1 = TC2 = null;

        String t_1 = null;
        String t_2 = null;
        //根据能匹配的最长的长度，以及当前的字，生成一个最长长度的2倍少一的字符串数组
//        String[] strNT = new String[2*lengthNT-1];
//        strNT[lengthNT - 1] = words[index];
//        for (int i = 1; i < lengthNT; i++) {
//			if(index - i >= 0){
//				strNT[lengthNT - 1 - i] = words[index - i];
//			}else{
//				strNT[lengthNT  - 1 - i] = "null";
//			}
//		}
//        for (int i = 1; i < lengthNT; i++) {
//			if(index + i < words.length){
//				strNT[lengthNT  - 1 + i] = words[index + i];
//			}else{
//				strNT[lengthNT  - 1 + i] = "null";
//			}
//		}
// 
//        String[] strNS = new String[2*lengthNS-1];
//        strNS[lengthNS - 1] = words[index];
//        for (int i = 1; i < lengthNS; i++) {
//			if(index - i >= 0){
//				strNS[lengthNS - 1 - i] = words[index - i];
//			}else{
//				strNS[lengthNS  - 1 - i] = "null";
//			}
//		}
//        for (int i = 1; i < lengthNS; i++) {
//			if(index + i < words.length){
//				strNS[lengthNS  - 1 + i] = words[index + i];
//			}else{
//				strNS[lengthNS  - 1 + i] = "null";
//			}
//		}
//        
//        String[] strNR = new String[2*lengthNR-1];
//        strNR[lengthNR - 1] = words[index];
//        for (int i = 1; i < lengthNR; i++) {
//			if(index - i >= 0){
//				strNR[lengthNR - 1 - i] = words[index - i];
//			}else{
//				strNR[lengthNR  - 1 - i] = "null";
//			}
//		}
//        for (int i = 1; i < lengthNR; i++) {
//			if(index + i < words.length){
//				strNR[lengthNR  - 1 + i] = words[index + i];
//			}else{
//				strNR[lengthNR  - 1 + i] = "null";
//			}
//		}
        
        w0 = words[index].toString();
        TC0 = FeatureTools.featureType(w0);
        if (words.length > index + 1) {
            w1 = words[index + 1].toString();
            TC1 = FeatureTools.featureType(w1);
            if (words.length > index + 2) {
                w2 = words[index + 2].toString();
                TC2 = FeatureTools.featureType(w2);
            }
        }

        if (index - 1 >= 0) {
            w_1 = words[index - 1].toString();
            TC_1 = FeatureTools.featureType(w_1);
            t_1 = tags[index - 1];
            if (index - 2 >= 0) {
                w_2 = words[index - 2].toString();
                TC_2 = FeatureTools.featureType(w_2);
                t_2 = tags[index - 2];
            }
        }

        List<String> features = new ArrayList<String>();
        // add the word itself

        if (c0Set) {
            features.add("w0=" + w0);
        }

        if (w_1 != null) {
            if (c_1Set) {
                features.add("w_1=" + w_1);
            }

            if (t_1Set) {
                features.add("t_1=" + t_1);
            }

            if (w_2 != null) {
                if (c_2Set) {
                    features.add("w_2=" + w_2);
                }

                if (t_2Set) {
                    features.add("t_2=" + t_2);
                }
                if(t_2t_1Set){
                	features.add("t_2t_1=" + t_2 + t_1);
                }
            }
        }

        if (w1 != null) {
            if (c1Set) {
                features.add("w1=" + w1);
            }
            if (w2 != null) {
                if (c2Set) {
                    features.add("w2=" + w2);
                }
            }
        }

        if (w_2 != null && w_1 != null) {
            if (c_2c_1Set) {
                features.add("w_2w_1=" + w_2 + w_1);
            }
        }

        if (w_1 != null) {
            if (c_1c0Set) {
                features.add("w_1w0=" + w_1 + w0);
            }
        }

        if (w1 != null) {
            if (c0c1Set) {
                features.add("w0w1=" + w0 + w1);
            }
        }

        if (w1 != null && w2 != null) {
            if (c1c2Set) {
                features.add("w1w2=" + w1 + w2);
            }
        }

        if (w_1 != null && w1 != null) {
            if (c_1c1Set) {
                features.add("w_1w1=" + w_1 + w1);
            }
        }

     // 增加标点符号的特征【应用了全角转半角的strq2b方法】
        if (PuSet)
        {
            if (FeatureTools.isChinesePunctuation(FeatureTools.strq2b(w0)))
                features.add("Pu=" + 1);
            else
                features.add("Pu=" + 0);
        }
        
        // 增加是否为数字字母等特征
        if(TC_2 != null && TC_1 != null && TC1 != null && TC2 != null){
        	if (TSet)
                features.add("T=" + TC_2 + TC_1 + TC0 + TC1 + TC2);
//        	System.out.println(tokens[index]+" "+TC_2 + TC_1 + TC0 + TC1 + TC2);
        }
        
     // 下面是增加和词典匹配后特征的情况
        //匹配机构名的词典
//        for (int i = lengthNT; i >= 2; i--) {
//        	List<String> feature;
//        	feature = find(i,strNT,"nt",dictionaryWordsNT);
//        	if(feature.size() == 0){
//        		
//        	}else{
//        		for (String string : feature) {
//					System.out.println(string);
//				}
//        		features.addAll(feature);
//        		feature.clear();
//        		break;
//        	}
//		}
//        
//       //匹配地名的词典
//        for (int i = lengthNS; i >= 2; i--) {
//        	List<String> feature;
//        	feature = find(i,strNS,"ns",dictionaryWordsNS);
//        	if(feature.size() == 0){
//        		
//        	}else{
//        		features.addAll(feature);
//        		feature.clear();
//        		break;
//        	}
//		}
        
      //匹配人名的词典
//        for (int i = lengthNR; i >= 2; i--) {
//        	List<String> feature;
//        	feature = find(i,strNR,"nr",dictionaryWordsNR);
//        	if(feature.size() == 0){
//        		
//        	}else{
//        		features.addAll(feature);
//        		feature.clear();
//        		break;
//        	}
//		}
        
        String[] contexts = features.toArray(new String[features.size()]);

        return contexts;
	}

	/**
	 * 单个字拼接成词语，和词典匹配
	 * @param length 词语的长度
	 * @param str 字的序列组成的数组
	 * @param flag 标志，代表是地名，人名还是机构名的词典，主要是对特征的生成有帮助
	 * @param words 词典，这里是人名，地名，机构名，其中的一个词典
	 */
	public List<String> find(int length, String[] str, String flag, Set<String> words){
		List<String> feature = new ArrayList<String>();
		int middle = (str.length - 1) / 2;
		for (int i = middle-length+1; i < middle+1; i++) {			
			String temp = "";
			int begin = -1;
			int end = -1;
			for (int j = i; j < length+i; j++) {
				temp += str[j];
				begin = j - length + 1;
				end = j;				
			}
//			System.out.println(temp);
			if(temp.contains("null")){
				
			}else{
				if(isDictionalWords(words,temp)){
//					System.out.println(str[middle]);
					if(begin == middle){
						if(Lt0Set){
							feature.add("L"+flag+"=" + length);
						}
						if(!str[middle-1].equals("null")){
							
		                	if (c_1t0Set)
		                    {
		                        feature.add("w_1b"+flag+"=" + str[middle-1]);
		                    }
		                }	                
		                if (c0t0Set)
		                {
		                    feature.add("w0b"+flag+"=" + str[middle]);
		                }
		                if(!str[middle+1].equals("null")){
		                	 if (c1t0Set)
		                     {
		                         feature.add("w1b"+flag+"=" + str[middle+1]);
		                     }
		                }	
//		                for (String string : feature) {
//							System.out.println(string);
//						}
					}else if(end == middle){					
						if(Lt0Set){						
							feature.add("L"+flag+"=" + length);					
						}					
						if(!str[middle-1].equals("null")){	                	
							if (c_1t0Set)	                    
							{	                       
								feature.add("w_1e"+flag+"=" + str[middle-1]);	                   
							}	                
						}	              	               
						if (c0t0Set)	                
						{	                    
							feature.add("w0e"+flag+"=" + str[middle]);	                
						}	               
						if(!str[middle+1].equals("null")){               	 
							if (c1t0Set)                    
							{                        
								feature.add("w1e="+flag+"=" + str[middle+1]);	                    
							}	                
						}
//						for (String string : feature) {
//							System.out.println(string);
//						}
					}else{				
						if(Lt0Set){					
							feature.add("L"+flag+"=" + length);					
						}					
						if(!str[middle-1].equals("null")){                	
							if (c_1t0Set)                    
							{                        
								feature.add("w_1m"+flag+"=" + str[middle-1]);	                    
							}	                
						}                	                
						if (c0t0Set)	                
						{	                   
							feature.add("w0m"+flag+"=" + str[middle]);	                
						}	                
						if(!str[middle+1].equals("null")){	                	 
							if (c1t0Set)	                     
							{	                         
								feature.add("w1m"+flag+"=" + str[middle+1]);	                     
							}	                						
						}						
					}	
					
				}	
				
			}		
		}		
//		int middle = (str.length - 1) / 2;
//		String temp = "";
//		for (int i = middle; i < middle+length; i++) {
//			temp += str[i];	
//		}
//		if(temp.contains("null")){
//			
//		}else{
//			if(isDictionalWords(words,temp)){
//				System.out.println(temp);
//				if(Lt0Set){					
//					feature.add("L"+flag+"=" + length);					
//				}					
//				if(!str[middle-1].equals("null")){                	
//					if (w_1t0Set)                    
//					{                        
//						feature.add("w_1"+flag+"=" + str[middle-1]);	                    
//					}	                
//				}                	                
//				if (w0t0Set)	                
//				{	                   
//					feature.add("w0"+flag+"=" + str[middle]);	                
//				}	                
//				if(!str[middle+1].equals("null")){	                	 
//					if (w1t0Set)	                     
//					{	                         
//						feature.add("w1"+flag+"=" + str[middle+1]);	                     
//					}	                						
//				}
//			}
//		}
		return feature;	
	}
	
	/**
	 * 判断当前的词语是否是词典中的词
	 * @param dictionaryWords 词典
	 * @param words 当前要判断的词语
	 * @return
	 */
	public boolean isDictionalWords(Set<String> dictionaryWords, String words){
		
		if(dictionaryWords.contains(words)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		
		return "NERCharacterContextGeneratorConf{" + "c_2Set=" + c_2Set + ", c_1Set=" + c_1Set + 
                ", c0Set=" + c0Set + ", c1Set=" + c1Set + ", c2Set=" + c2Set + 
                ", c_2c_1Set=" + c_2c_1Set + ", c_1c0Set=" + c_1c0Set + 
                ", c0c1Set=" + c0c1Set + ", c1c2Set=" + c1c2Set + 
                ", c_1c1Set=" + c_1c1Set + ", PuSet=" + PuSet + 
                ", TSet=" + TSet + ", t_2Set=" + t_2Set + 
                ", t_1Set=" + t_1Set + ", t_2t_1Set=" + t_2t_1Set +  
                '}';
	}
}
