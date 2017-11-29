package com.wxw.word.feature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.wxw.ner.tools.FeatureTools;
import com.wxw.ner.wordandpos.feature.NERWordAndPosContextGeneratorConf;

/**
 * 为基于分词的命名实体识别生成特征
 * @author 王馨苇
 *
 */
public class NERWordContextGeneratorConf implements NERWordContextGenerator{

	private boolean w_2Set;
    private boolean w_1Set;
    private boolean w0Set;
    private boolean w1Set;
    private boolean w2Set;
    private boolean w_2w_1Set;
    private boolean w_1w0Set;
    private boolean w0w1Set;
    private boolean w1w2Set;
    private boolean w_1w1Set;
    private boolean t_1Set;
    private boolean t_2Set;
    private boolean t_2t_1Set;
  //增加的字典特征的控制变量
    private boolean Lt0Set;
    private boolean w_1t0Set;
    private boolean w0t0Set;
    private boolean w1t0Set;
    
    public NERWordContextGeneratorConf() throws IOException {
    	Properties featureConf = new Properties();
        InputStream featureStream = NERWordContextGeneratorConf.class.getClassLoader().getResourceAsStream("com/wxw/ner/run/feature.properties");
        featureConf.load(featureStream);
        
        init(featureConf);
	}
    
    /**
	 * 构造
	 * @param properties 配置文件
	 */
	public NERWordContextGeneratorConf(Properties properties){
        
        init(properties);
	}
    
    /**
     * 初始化配置文件
     * @param featureConf 配置文件
     */
    private void init(Properties config) {
    	w_2Set = (config.getProperty("word.w_2", "true").equals("true"));
        w_1Set = (config.getProperty("word.w_1", "true").equals("true"));
        w0Set = (config.getProperty("word.w0", "true").equals("true"));
        w1Set = (config.getProperty("word.w1", "true").equals("true"));
        w2Set = (config.getProperty("word.w2", "true").equals("true"));

        w_2w_1Set = (config.getProperty("word.w_2w_1", "true").equals("true"));
        w_1w0Set = (config.getProperty("word.w_1w0", "true").equals("true"));
        w0w1Set = (config.getProperty("word.w0w1", "true").equals("true"));
        w1w2Set = (config.getProperty("word.w1w2", "true").equals("true"));

        w_1w1Set = (config.getProperty("word.w_1w1", "true").equals("true"));
        
        t_1Set = (config.getProperty("word.t_1", "true").equals("true"));
        t_2Set = (config.getProperty("word.t_2", "true").equals("true"));
        t_2t_1Set = (config.getProperty("word.t_2t_1", "true").equals("true"));  
        
     // 获取配置文件中的字典特征的设置值
        Lt0Set = (config.getProperty("word.Lt0", "true").equals("true"));
        w_1t0Set = (config.getProperty("word.w_1t0", "true").equals("true"));
        w0t0Set = (config.getProperty("word.w0t0", "true").equals("true"));
        w1t0Set = (config.getProperty("word.w1t0", "true").equals("true"));
	}

	/**
	 * 生成上下文特征
	 * @param index 索引
	 * @param words 词语
	 * @param tags 命名实体标记
	 * @return
	 */
	@Override
	public String[] getContext(int index, String[] words, String[] tags, Object[] ac) {
		
		return getContext(index, words, tags);
	}

	/**
	 * 生成上下文特征
	 * @param index 索引
	 * @param words 词语
	 * @param tags 命名实体标记
	 * @return
	 */
	private String[] getContext(int index, String[] words, String[] tags) {
		String w1, w2, w3,w4,w5,w6, w0, w_1, w_2,w_3,w_4,w_5,w_6;
        w1 = w2 = w3 = w4 = w5 = w6 = w0 = w_1 = w_2 = w_3= w_4 = w_5 = w_6 =  null;
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
        if (words.length > index + 1) {
            w1 = words[index + 1].toString();
            if (words.length > index + 2) {
                w2 = words[index + 2].toString();
            }
        }

        if (index - 1 >= 0) {
            w_1 = words[index - 1].toString();
            t_1 = tags[index - 1];
            if (index - 2 >= 0) {
                w_2 = words[index - 2].toString();
                t_2 = tags[index - 2];
            }
        }

        List<String> features = new ArrayList<String>();

        if (w0Set) {
            features.add("w0=" + w0);
        }

        if (w_1 != null) {
            if (w_1Set) {
                features.add("w_1=" + w_1);
            }

            if (t_1Set) {
                features.add("t_1=" + t_1);
            }

            if (w_2 != null) {
                if (w_2Set) {
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
            if (w1Set) {
                features.add("w1=" + w1);
            }
            if (w2 != null) {
                if (w2Set) {
                    features.add("w2=" + w2);
                }
            }
        }

        if (w_2 != null && w_1 != null) {
            if (w_2w_1Set) {
                features.add("w_2w_1=" + w_2 + w_1);
            }
        }

        if (w_1 != null) {
            if (w_1w0Set) {
                features.add("w_1w0=" + w_1 + w0);
            }
        }

        if (w1 != null) {
            if (w0w1Set) {
                features.add("w0w1=" + w0 + w1);
            }
        }

        if (w1 != null && w2 != null) {
            if (w1w2Set) {
                features.add("w1w2=" + w1 + w2);
            }
        }

        if (w_1 != null && w1 != null) {
            if (w_1w1Set) {
                features.add("w_1w1=" + w_1 + w1);
            }
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
							
		                	if (w_1t0Set)
		                    {
		                        feature.add("w_1b"+flag+"=" + str[middle-1]);
		                    }
		                }	                
		                if (w0t0Set)
		                {
		                    feature.add("w0b"+flag+"=" + str[middle]);
		                }
		                if(!str[middle+1].equals("null")){
		                	 if (w1t0Set)
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
							if (w_1t0Set)	                    
							{	                       
								feature.add("w_1e"+flag+"=" + str[middle-1]);	                   
							}	                
						}	              	               
						if (w0t0Set)	                
						{	                    
							feature.add("w0e"+flag+"=" + str[middle]);	                
						}	               
						if(!str[middle+1].equals("null")){               	 
							if (w1t0Set)                    
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
							if (w_1t0Set)                    
							{                        
								feature.add("w_1m"+flag+"=" + str[middle-1]);	                    
							}	                
						}                	                
						if (w0t0Set)	                
						{	                   
							feature.add("w0m"+flag+"=" + str[middle]);	                
						}	                
						if(!str[middle+1].equals("null")){	                	 
							if (w1t0Set)	                     
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
		return "NERWordContextGeneratorConf{" + "w_2Set=" + w_2Set + ", w_1Set=" + w_1Set + 
                ", w0Set=" + w0Set + ", w1Set=" + w1Set + ", w2Set=" + w2Set + 
                ", w_2w_1Set=" + w_2w_1Set + ", w_1w0Set=" + w_1w0Set + 
                ", w0w1Set=" + w0w1Set + ", w1w2Set=" + w1w2Set + 
                ", w_1w1Set=" + w_1w1Set +
                ", t_2Set=" + t_2Set + 
                ", t_1Set=" + t_1Set + ", t_2t_1Set=" + t_2t_1Set +  
                '}';
	}
}
