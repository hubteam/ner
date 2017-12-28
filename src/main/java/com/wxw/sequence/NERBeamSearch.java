package com.wxw.sequence;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import com.wxw.wordandpos.NERWordAndPosBeamSearchContextGenerator;

import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.Cache;
import opennlp.tools.util.Sequence;

/**
 * 得到结果序列的具体实现类
 * @author 王馨苇
 *
 * @param <T>
 */
public class NERBeamSearch<T> implements NERSequenceClassificationModel<T> {

	public static final String BEAM_SIZE_PARAMETER = "BeamSize";
	private static final Object[] EMPTY_ADDITIONAL_CONTEXT = new Object[0];
	protected int size;
	protected MaxentModel model;
	private double[] probs;
	private Cache<String[], double[]> contextsCache;
	private static final int zeroLog = -100000;

	public NERBeamSearch(int size, MaxentModel model) {
		this(size, model, 0);
	}

	public NERBeamSearch(int size, MaxentModel model, int cacheSize) {
		this.size = size;
		this.model = model;
		if (cacheSize > 0) {
			this.contextsCache = new Cache(cacheSize);
		}

		this.probs = new double[model.getNumOutcomes()];
	}
	
	/**
	 * 得到最好的序列
	 * @param arg0 词语
	 * @param arg1 词性
	 * @param arg3 额外的信息
	 * @param arg4 上下文生成器
	 * @param arg5 序列的验证器
	 * @return 最好的序列
	 */
	public Sequence bestSequence(T[] arg0, T[] arg1, Object[] arg3, NERWordAndPosBeamSearchContextGenerator<T> arg4,
			NERSequenceValidator<T> arg5) {
		Sequence[] sequences = this.bestSequences(1, arg0, arg1, arg3, arg4, arg5);
		return sequences.length > 0 ? sequences[0] : null;
	}

	/**
	 * 得到最好的numSequences个序列
	 * @param numSequences 最好的几个序列
	 * @param sequence 词语
	 * @param pos 词性
	 * @param additionalContext 额外的信息
	 * @param minSequenceScore 标记所得最小的分数的限制
	 * @param cg 上下文生成器
	 * @param validator 序列的验证器
	 * @return
	 */
	public Sequence[] bestSequences(int numSequences, T[] sequence, T[] pos, Object[] additionalContext, double minSequenceScore,
			NERWordAndPosBeamSearchContextGenerator<T> cg, NERSequenceValidator<T> validator) {
		PriorityQueue<Sequence> prev = new PriorityQueue<Sequence>(this.size);
		PriorityQueue<Sequence> next = new PriorityQueue<Sequence>(this.size);
		prev.add(new Sequence());
		if (additionalContext == null) {
			additionalContext = EMPTY_ADDITIONAL_CONTEXT;
		}

		int numSeq;
		int seqIndex;
		for (numSeq = 0; numSeq < sequence.length; ++numSeq) {
			int topSequences = Math.min(this.size, prev.size());

			for (seqIndex = 0; prev.size() > 0 && seqIndex < topSequences; ++seqIndex) {
				Sequence top = (Sequence) prev.remove();
				List<String> tmpOutcomes = top.getOutcomes();
				String[] outcomes = (String[]) tmpOutcomes.toArray(new String[tmpOutcomes.size()]);
				String[] contexts = cg.getContext(numSeq, sequence, pos, outcomes, additionalContext);
				double[] scores;
				if (this.contextsCache != null) {
					scores = (double[]) this.contextsCache.get(contexts);
					if (scores == null) {
						scores = this.model.eval(contexts, this.probs);
						this.contextsCache.put(contexts, scores);
					}
				} else {
					scores = this.model.eval(contexts, this.probs);
				}

				double[] temp_scores = new double[scores.length];
				System.arraycopy(scores, 0, temp_scores, 0, scores.length);
				Arrays.sort(temp_scores);
				double min = temp_scores[Math.max(0, scores.length - this.size)];

				int p;
				String out;
				Sequence ns;
				for (p = 0; p < scores.length; ++p) {
					if (scores[p] >= min) {
						out = this.model.getOutcome(p);
						if (validator.validSequence(numSeq, sequence, pos, outcomes, out)) {
							ns = new Sequence(top, out, scores[p]);
							if (ns.getScore() > minSequenceScore) {
								next.add(ns);
							}
						}
					}
				}

				if (next.size() == 0) {
					for (p = 0; p < scores.length; ++p) {
						out = this.model.getOutcome(p);
						if (validator.validSequence(numSeq, sequence, pos, outcomes, out)) {
							ns = new Sequence(top, out, scores[p]);
							if (ns.getScore() > minSequenceScore) {
								next.add(ns);
							}
						}
					}
				}
			}

			prev.clear();
			PriorityQueue<Sequence> tmp = prev;
			prev = next;
			next = tmp;
		}

		numSeq = Math.min(numSequences, prev.size());
		Sequence[] arg24 = new Sequence[numSeq];

		for (seqIndex = 0; seqIndex < numSeq; ++seqIndex) {
			arg24[seqIndex] = (Sequence) prev.remove();
		}

		return arg24;
	}

	/**
	 * 得到最好的arg0个序列
	 * @param arg0 最好的几个序列
	 * @param arg1 词语
	 * @param arg2 词性
	 * @param arg4 额外的信息
	 * @param arg5 上下文生成器
	 * @param arg6 序列的验证器
	 * @return
	 */
	public Sequence[] bestSequences(int arg0, T[] arg1, T[] arg2, Object[] arg4, NERWordAndPosBeamSearchContextGenerator<T> arg5,
			NERSequenceValidator<T> arg6) {
		return this.bestSequences(arg0, arg1, arg2, arg4, -100000.0D, arg5, arg6);
	}

	/**
	 * 得到最好的结果
	 * @return
	 */
	public String[] getOutcomes() {
		String[] outcomes = new String[this.model.getNumOutcomes()];

		for (int i = 0; i < this.model.getNumOutcomes(); ++i) {
			outcomes[i] = this.model.getOutcome(i);
		}

		return outcomes;
	}

}
