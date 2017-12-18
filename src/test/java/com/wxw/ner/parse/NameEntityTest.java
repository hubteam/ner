package com.wxw.ner.parse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.namedentity.NamedEntity;
import com.wxw.word.model.NERWordME;

/**
 * 测试得到的结果
 * @author 王馨苇
 *
 */
public class NameEntityTest {

	private String outputTags;
	private String sentence;
	private String[] ners;
	private String[] words;
	private List<NamedEntity> ner = new ArrayList<>();
	private NERWordME nwm;
	
	@Before
	public void setUp(){
		nwm = new NERWordME();
		outputTags = "b_nr e_nr o o o o o o o b_nt e_nt o o o o o o o o o o o o o o o b_nr e_nr o o b_nr e_nr o o o o s_ns o o o b_nr e_nr o o o o o o o o o o o";
		ners = outputTags.split(" ");
		sentence = "林 政志 供认 了 他 先后 ５ 次 从 玉溪 卷烟厂 套购 ８０００ 多 件 卷烟 、 牟取暴利 的 事实 ， 并 交待 他 多次 给 褚 时健 的 妻子 马 静芬 、 妻 妹 马 静芳 、 妻 弟 马 建华 等 人 送 钱 送 物 ９０ 余 万 元 。";
		words = sentence.split(" ");
		for (int i = 0; i < ners.length; i++) {
			String flag;
			if(ners[i].equals("o")){
				flag = "o";
			}else{
				flag = ners[i].split("_")[1];
			}
			if(ner.size() == 0){
				ner.add(nwm.getNer(0, ners, words, flag));
			}else{
				ner.add(nwm.getNer(i, ners, words, flag));
			}
			i = ner.get(ner.size()-1).getEnd();
		}
	}
	
	@Test
	public void test(){
		for (int i = 0; i < ner.size(); i++) {
			System.out.println(ner.get(i).getString()+"/"+ner.get(i).getType());
		}
	}
}
