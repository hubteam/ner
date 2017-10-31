package com.wxw.ner.sequence;

/**
 * 验证输出序列是否合法的接口
 * @author 王馨苇
 *
 * @param <T>
 */
public interface NERSequenceValidator<T> {

	/**
	 * 验证序列是否正确
	 * @param i 当前词语下标
	 * @param words 词语
	 * @param poses 词性
	 * @param tags 命名实体识别标记
	 * @param out 得到的下一个字符的输出结果
	 */
	boolean validSequence(int i, T[] words, T[] poses, String[] tags, String out);
}
