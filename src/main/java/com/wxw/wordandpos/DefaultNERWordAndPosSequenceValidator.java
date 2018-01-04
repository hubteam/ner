package com.wxw.wordandpos;

import opennlp.tools.util.SequenceValidator;

/**
 * 为基于词性标注命名实体识别的解码校验
 * @author 王馨苇
 *
 */
public class DefaultNERWordAndPosSequenceValidator implements SequenceValidator<String> {

	/**
	 * 验证序列是否合法
	 * @param i 当前下标
	 * @param words 当前词语序列
	 * @param tags 当前位置之前，已求解出来的序列
	 * @param out 当前下标对应的结果，即要检查是否合法的结果
	 */
	public boolean validSequence(int i, String[] words, String[] tags, String out) {
		if(words.length == 1){
			if(out.equals("o") || out.equals("s_nt") || out.equals("s_nr") || out.equals("s_ns") || out.equals("s_nz")){
				return true;
			}
		}else if(words.length > 1){
			if(i == 0){
				if(out.equals("o") || out.equals("b_nt") || out.equals("b_nr") || out.equals("b_ns") || out.equals("b_nz")){
					return true;
				}
			}else if(i - 1 >= 0 && i < words.length - 1){
				if(out.equals("o") || out.equals("b_nr") || out.equals("b_nt") || out.equals("b_ns") || out.equals("b_nz")
						|| out.equals("s_nr") || out.equals("s_nt") || out.equals("s_ns") || out.equals("s_nz")){
					if(tags[i-1].equals("o") || tags[i-1].equals("s_nt") || tags[i-1].equals("s_nr") || tags[i-1].equals("s_ns") || tags[i-1].equals("s_nz")
							|| tags[i-1].equals("e_nt") || tags[i-1].equals("e_nr") || tags[i-1].equals("e_ns") || tags[i-1].equals("e_nz")){
						return true;
					}
				}else if(out.equals("m_nr")){
					if(tags[i-1].equals("b_nr")){
						return true;
					}
				}else if(out.equals("e_nr")){
					if(tags[i-1].equals("m_nr") || tags[i-1].equals("b_nr")){
						return true;
					}
				}
				else if(out.equals("m_nt")){
					if(tags[i-1].equals("b_nt")){
						return true;
					}
				}else if(out.equals("e_nt")){
					if(tags[i-1].equals("m_nt") || tags[i-1].equals("b_nt")){
						return true;
					}
				}else if(out.equals("m_ns")){
					if(tags[i-1].equals("b_ns")){
						return true;
					}
				}else if(out.equals("e_ns")){
					if(tags[i-1].equals("m_ns") || tags[i-1].equals("b_ns")){
						return true;
					}
				}else if(out.equals("m_nz")){
					if(tags[i-1].equals("b_nz")){
						return true;
					}
				}else if(out.equals("e_nz")){
					if(tags[i-1].equals("m_nz") || tags[i-1].equals("b_nz")){
						return true;
					}
				}
			}else if(i == words.length - 1){
				if(out.equals("e_nr")){
					if(tags[i-1].equals("m_nr") || tags[i-1].equals("b_nr")){
						return true;
					}
				}else if(out.equals("e_nt")){
					if(tags[i-1].equals("m_nt") || tags[i-1].equals("b_nt")){
						return true;
					}
				}else if(out.equals("e_ns")){
					if(tags[i-1].equals("m_ns") || tags[i-1].equals("b_ns")){
						return true;
					}
				}else if(out.equals("e_nz")){
					if(tags[i-1].equals("m_nz") || tags[i-1].equals("b_nz")){
						return true;
					}
				}else if(out.equals("o") || out.equals("s_nr") || out.equals("s_nt") || out.equals("s_ns") || out.equals("s_nz")){
					if(tags[i-1].equals("o") || tags[i-1].equals("s_nt") || tags[i-1].equals("s_nr") || tags[i-1].equals("s_ns") || tags[i-1].equals("s_nz")
							|| tags[i-1].equals("e_nt") || tags[i-1].equals("e_nr") || tags[i-1].equals("e_ns") || tags[i-1].equals("e_nz")){
						return true;
					}
				}
			}
		}
		return false;
	}
}
