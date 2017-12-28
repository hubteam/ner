package com.wxw.wordandpos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 根据配置文件得到特征
 * @author 王馨苇
 *
 */
public class NERWordAndPosContextGeneratorConf implements NERWordAndPosContextGenerator{

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
   
    private boolean p_2Set;
    private boolean p_1Set;
    private boolean p0Set;
    private boolean p1Set;
    private boolean p2Set;
    private boolean p_2p_1Set;
    private boolean p_1p0Set;
    private boolean p0p1Set;
    private boolean p1p2Set;
    private boolean p_2p_1p0Set;
    private boolean p0p1p2Set;

    private boolean p0w0Set;
    
    private boolean t_1Set;
    private boolean t_2Set;
    private boolean t_2t_1Set;
  //增加的字典特征的控制变量
    private boolean Lt0Set;
    private boolean w_1t0Set;
    private boolean w0t0Set;
    private boolean w1t0Set;
	/**
	 * 构造
	 * @throws IOException
	 */
	public NERWordAndPosContextGeneratorConf() throws IOException{
		Properties featureConf = new Properties();
        InputStream featureStream = NERWordAndPosContextGeneratorConf.class.getClassLoader().getResourceAsStream("com/wxw/ner/run/feature.properties");
        featureConf.load(featureStream);
        
        init(featureConf);
	}
	
	/**
	 * 构造
	 * @param properties 配置文件
	 */
	public NERWordAndPosContextGeneratorConf(Properties properties){
        
        init(properties);
	}
	
	/**
	 * 根据配置文件的信息初始化特征
	 * @param config 配置文件
	 */
	private void init(Properties config) {
		w_2Set = (config.getProperty("wordpos.w_2", "true").equals("true"));
        w_1Set = (config.getProperty("wordpos.w_1", "true").equals("true"));
        w0Set = (config.getProperty("wordpos.w0", "true").equals("true"));
        w1Set = (config.getProperty("wordpos.w1", "true").equals("true"));
        w2Set = (config.getProperty("wordpos.w2", "true").equals("true"));

        w_2w_1Set = (config.getProperty("wordpos.w_2w_1", "true").equals("true"));
        w_1w0Set = (config.getProperty("wordpos.w_1w0", "true").equals("true"));
        w0w1Set = (config.getProperty("wordpos.w0w1", "true").equals("true"));
        w1w2Set = (config.getProperty("wordpos.w1w2", "true").equals("true"));

        w_1w1Set = (config.getProperty("wordpos.w_1w1", "true").equals("true"));
        
        p_2Set = (config.getProperty("wordpos.p_2", "true").equals("true"));
        p_1Set = (config.getProperty("wordpos.p_1", "true").equals("true"));
        p0Set = (config.getProperty("wordpos.p0", "true").equals("true"));
        p1Set = (config.getProperty("wordpos.p1", "true").equals("true"));
        p2Set = (config.getProperty("wordpos.p2", "true").equals("true"));
        
        p_2p_1Set = (config.getProperty("wordpos.p_2p_1", "true").equals("true"));
        p_1p0Set = (config.getProperty("wordpos.p_1p0", "true").equals("true"));
        p0p1Set = (config.getProperty("wordpos.p0p1", "true").equals("true"));
        p1p2Set = (config.getProperty("wordpos.p1p2", "true").equals("true"));
        
        p_2p_1p0Set = (config.getProperty("wordpos.p_2p_1p0", "true").equals("true"));
        p0p1p2Set = (config.getProperty("wordpos.p0p1p2", "true").equals("true"));
        
        p0w0Set = (config.getProperty("wordpos.p0w0", "true").equals("true"));

        t_1Set = (config.getProperty("wordpos.t_1", "true").equals("true"));
        t_2Set = (config.getProperty("wordpos.t_2", "true").equals("true"));
        t_2t_1Set = (config.getProperty("wordpos.t_2t_1", "true").equals("true"));  
        
     // 获取配置文件中的字典特征的设置值
        Lt0Set = (config.getProperty("wordpos.Lt0", "true").equals("true"));
        w_1t0Set = (config.getProperty("wordpos.w_1t0", "true").equals("true"));
        w0t0Set = (config.getProperty("wordpos.w0t0", "true").equals("true"));
        w1t0Set = (config.getProperty("wordpos.w1t0", "true").equals("true"));
        
	}
	
	public String[] getContext(int index, String[] words, String[] poses, String[] tags, Object[] ac) {
		// TODO Auto-generated method stub
		return getContext(index,words,poses,tags);
	}

	public String[] getContext(int index, String[] words, String[] poses, String[] tags) {
		String w1, w2, w0, w_1, w_2;
        w1 = w2 = w0 = w_1 = w_2 = null;
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
            }
        }

        List<String> features = new ArrayList<String>();

        if(p0w0Set){
        	features.add("p0w0=" + p0+w0);
        }
        
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
                if(t_2t_1Set){
                	features.add("t_2t_1=" + t_2 + t_1);
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

        if (w_2 != null && w_1 != null) {
            if (w_2w_1Set) {
                features.add("w_2w_1=" + w_2 + w_1);
            }
            if (p_2p_1Set) {
                features.add("p_2p_1=" + p_2 + p_1);
            }
        }

        if (w_1 != null) {
            if (w_1w0Set) {
                features.add("w_1w0=" + w_1 + w0);
            }
            if (p_1p0Set) {
                features.add("p_1p0=" + p_1 + p0);
            }
        }

        if (w1 != null) {
            if (w0w1Set) {
                features.add("w0w1=" + w0 + w1);
            }
            if (p0p1Set) {
                features.add("p0p1=" + p0 + p1);
            }
        }

        if (w1 != null && w2 != null) {
            if (w1w2Set) {
                features.add("w1w2=" + w1 + w2);
            }
            if (p1p2Set) {
                features.add("p1p2=" + p1 + p2);
            }
        }

        if (w_1 != null && w1 != null) {
            if (w_1w1Set) {
                features.add("w_1w1=" + w_1 + w1);
            }
        }
        
        if(w1 != null && w2 != null){
        	if(p0p1p2Set){
                features.add("p0p1p2=" + p0 + p1 + p2);
        	}
        }
        if(w_1 != null && w_2 != null){
        	if(p_2p_1p0Set){
                features.add("p_2p_1p0=" + p_2 + p_1 + p0);
        	}
        }
        String[] contexts = features.toArray(new String[features.size()]);

        return contexts;
	}

	@Override
	public String toString() {
		
		return "NERWordAndPosContextGeneratorConf{" + "w_2Set=" + w_2Set + ", w_1Set=" + w_1Set + 
                ", w0Set=" + w0Set + ", w1Set=" + w1Set + ", w2Set=" + w2Set + 
                ", w_2w_1Set=" + w_2w_1Set + ", w_1w0Set=" + w_1w0Set + 
                ", w0w1Set=" + w0w1Set + ", w1w2Set=" + w1w2Set + ", w_1w1Set=" + w_1w1Set + 
                ", t_2Set=" + t_2Set + ", t_1Set=" + t_1Set + ", t_2t_1Set=" + t_2t_1Set +                
                ", p_2Set=" + p_2Set + ", p_1Set=" + p_1Set + ", p0Set=" + p0Set + 
                ", p1Set=" + p1Set + ", p2Set=" + p2Set + 
                ", p_2p_1Set=" + p_2p_1Set + ", p_1p0Set=" + p_1p0Set + ", p0p1Set=" + p0p1Set + 
                ", p1p2Set=" + p1p2Set + ", p_2p_1p0Set=" + p_2p_1p0Set + ", p0p1p2Set=" + p0p1p2Set + 
                ", p0w0Set=" + p0w0Set + 
                '}';
	}
}
