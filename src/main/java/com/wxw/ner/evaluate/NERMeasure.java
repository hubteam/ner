package com.wxw.ner.evaluate;

/**
 * 计算指标
 * @author 王馨苇
 *
 */
public class NERMeasure {

	/**
     * |selected| = true positives + false positives <br>
     * 预测的样本数
     */
    private long selected;

    /**
     * |target| = true positives + false negatives <br>
     * 参考的样本数
     */
    private long target;
    //人名
    private long truePositivenr;
    private long selectednr;
    private long targetnr;
    //机构名
    private long truePositivent;
    private long selectednt;
    private long targetnt;
    //地名
    private long truePositivens;
    private long selectedns;
    private long targetns;
    //专有名词
    private long truePositivenz;
    private long selectednz;
    private long targetnz;
    
    /**
     * 预测正确的个数
     */
    private long truePositive;
    
    /**
	 * 更新计算指标的一些变量【词性标注的指标】
	 * @param wordsRef 参考的分词结果
	 * @param tagsRef 参考的词性标记
	 * @param wordsPre 预测的分词结果
	 * @param tagsPre 预测的词性标记
	 */
	public void update(String[] wordsRef, String[] tagsRef, String[] wordsPre, String[] tagsPre) {
		//定义变量记录当前扫描的总长度
    	int countRef = 0,countPre = 0;
    	//记录当前所在的词的位置
    	int i = 0,j = 0;
    	//记录i的前一次的值
    	int iPre = -1;
    	if(wordsRef.length > 0 && wordsPre.length > 0){
    		while(wordsRef[i] != null || !("".equals(wordsRef[i]))|| wordsPre[j] != null || !("".equals(wordsPre[j]))){
        		countRef += wordsRef[i].length();
        		countPre += wordsPre[j].length();
        		
        		//匹配的情况
        		if((wordsRef[i] == wordsPre[j] || wordsRef[i].equals(wordsPre[j]))){

        			if((!tagsRef[i].equals("o") && !tagsPre[j].equals("o")) && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
        					
        				truePositive++;        				
        			}       			
        			if(tagsRef[i].equals("nr") && tagsPre[j].equals("nr") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
    					
        				truePositivenr++;        				
        			} 
 
        			if(tagsRef[i].equals("ns") && tagsPre[j].equals("ns") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
    					
        				truePositivens++;  
        			}

        			if(tagsRef[i].equals("nt") && tagsPre[j].equals("nt") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
    					
        				truePositivent++;  
        			}
        			
        			if(tagsRef[i].equals("nz") && tagsPre[j].equals("nz") && (tagsRef[i] == tagsPre[j] || tagsRef[i].equals(tagsPre[j]))){
    					
        				truePositivenz++;  
        			}

        			iPre = i;
    				//两个字符串同时向后扫描
        			i++;j++;
        			//为了防止：已经到达边界了，还用references[i]或者predictions[i]来判断，此时越界了
    				if(i >= wordsRef.length || j >= wordsPre.length)
    					break;
   
        		}else{
        			//不匹配的情况，则需要比较当前扫过的总长度
        			//（1）：长度长的那个不动，长度短的那个要继续向前扫描比较   			
        			if(countRef > countPre){
    					iPre = i;
        				j++;
    					countRef -= wordsRef[i].length();
    					if(j >= wordsPre.length)
    						break;
    					//（2）：长度相等的时候，二者都需要向前扫描
    				}else if(countRef == countPre){
    					iPre = i;
    					i++;j++;
    					if(i >= wordsRef.length || j >= wordsPre.length)
    						break;
    					//（1）：长度长的那个不动，长度短的那个要继续向前扫描比较
    				}else if(countRef < countPre){
    					iPre = i;
    					i++;
    					countPre -= wordsPre[j].length();
    					if(i >= wordsRef.length)
    						break;
    				}
        		}
        	}
    	}
    	for (int k = 0; k < tagsPre.length; k++) {
    		if(!tagsPre[k].equals("o")){
    			selected++;
    		}   		
    		if(tagsPre[k].equals("nr")){
    			selectednr++;
    		}
    		if(tagsPre[k].equals("ns")){
    			selectedns++;
    		}
    		if(tagsPre[k].equals("nt")){
    			selectednt++;
    		}
    		if(tagsPre[k].equals("nz")){
    			selectednz++;
    		}
    		
		}
    	for (int k = 0; k < tagsRef.length; k++) {
    		if(tagsRef[k].equals("nr")){
    			targetnr++;
    		}
    		if(tagsRef[k].equals("ns")){
    			targetns++;
    		}
    		if(tagsRef[k].equals("nt")){
    			targetnt++;
    		}
    		if(tagsRef[k].equals("nz")){
    			targetnz++;
    		}
    		if(!tagsRef[k].equals("o")){
    			target++;
    		}
		}
	}
	
	/**
	 * 命名实体识别准确率
	 * @return
	 */
	public double getPrecisionScore() {
		return selected > 0 ? (double) truePositive / (double) selected : 0;
	}

	/**
	 * 命名实体识别召回率
	 * @return
	 */
	public double getRecallScore() { 
		return target > 0 ? (double) truePositive / (double) target : 0;
	}
	
	/**
	 * 命名实体识别F值
	 * @return
	 */
	public double getMeasure() {

        if (getPrecisionScore() + getRecallScore() > 0) {
            return 2 * (getPrecisionScore() * getRecallScore())
                    / (getPrecisionScore() + getRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }

	/**
	 * 人名实体识别准确率
	 * @return
	 */
	public double getNRPrecisionScore() {
		return selectednr > 0 ? (double) truePositivenr / (double) selectednr : 0;
	}

	/**
	 * 人名实体识别召回率
	 * @return
	 */
	public double getNRRecallScore() { 
		return targetnr > 0 ? (double) truePositivenr / (double) targetnr : 0;
	}
	
	/**
	 * 人名实体识别F值
	 * @return
	 */
	public double getNRMeasure() {

        if (getNRPrecisionScore() + getNRRecallScore() > 0) {
            return 2 * (getNRPrecisionScore() * getNRRecallScore())
                    / (getNRPrecisionScore() + getNRRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }
	
	/**
	 * 地名实体识别准确率
	 * @return
	 */
	public double getNSPrecisionScore() {
		return selectedns > 0 ? (double) truePositivens / (double) selectedns : 0;
	}

	/**
	 * 地名实体识别召回率
	 * @return
	 */
	public double getNSRecallScore() { 
		return targetns > 0 ? (double) truePositivens / (double) targetns : 0;
	}
	
	/**
	 * 地名实体识别F值
	 * @return
	 */
	public double getNSMeasure() {

        if (getNSPrecisionScore() + getNSRecallScore() > 0) {
            return 2 * (getNSPrecisionScore() * getNSRecallScore())
                    / (getNSPrecisionScore() + getNSRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }
	
	/**
	 * 机构名实体识别准确率
	 * @return
	 */
	public double getNTPrecisionScore() {
		return selectednt > 0 ? (double) truePositivent / (double) selectednt : 0;
	}

	/**
	 * 机构名实体识别召回率
	 * @return
	 */
	public double getNTRecallScore() { 
		return targetnt > 0 ? (double) truePositivent / (double) targetnt : 0;
	}
	
	/**
	 * 机构名实体识别F值
	 * @return
	 */
	public double getNTMeasure() {

        if (getNTPrecisionScore() + getNTRecallScore() > 0) {
            return 2 * (getNTPrecisionScore() * getNTRecallScore())
                    / (getNTPrecisionScore() + getNTRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }
	
	/**
	 * 专有名词实体识别准确率
	 * @return
	 */
	public double getNZPrecisionScore() {
		return selectednz > 0 ? (double) truePositivenz / (double) selectednz : 0;
	}

	/**
	 * 专有名词实体识别召回率
	 * @return
	 */
	public double getNZRecallScore() { 
		return targetnz > 0 ? (double) truePositivenz / (double) targetnz : 0;
	}
	
	/**
	 * 专有名词实体识别F值
	 * @return
	 */
	public double getNZMeasure() {

        if (getNZPrecisionScore() + getNZRecallScore() > 0) {
            return 2 * (getNZPrecisionScore() * getNZRecallScore())
                    / (getNZPrecisionScore() + getNZRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }
	
	@Override
	public String toString() {
		return "Precision: " + Double.toString(getPrecisionScore()) + "\n"
        + "Recall: " + Double.toString(getRecallScore()) + "\n" 
		+ "F-Measure: "+ Double.toString(getMeasure()) + "\n"
		+ "NRPrecision: " + Double.toString(getNRPrecisionScore()) + "\n"
        + "NRRecall: " + Double.toString(getNRRecallScore()) + "\n" 
		+ "NRF-Measure: "+ Double.toString(getNRMeasure()) + "\n"
		+ "NSPrecision: " + Double.toString(getNSPrecisionScore()) + "\n"
        + "NSRecall: " + Double.toString(getNSRecallScore()) + "\n" 
		+ "NSF-Measure: "+ Double.toString(getNSMeasure()) + "\n"
		+ "NTPrecision: " + Double.toString(getNTPrecisionScore()) + "\n"
		+ "NTRecall: " + Double.toString(getNTRecallScore()) + "\n" 
		+ "NTF-Measure: "+ Double.toString(getNTMeasure()) + "\n"
		+ "NZPrecision: " + Double.toString(getNZPrecisionScore()) + "\n"
		+ "NZRecall: " + Double.toString(getNZRecallScore()) + "\n" 
		+ "NZF-Measure: "+ Double.toString(getNZMeasure()) + "\n";
	}
}
