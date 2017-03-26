package com.uiuc.cs498.netpromoter.analyzer.busobj;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.mllib.linalg.Vector;

public class TweetAnalysis {

	private String tweetText;
	private Integer score;
	private Vector probabilities;
	private Map<String, Integer> counts = new HashMap<>();
	
	public TweetAnalysis(){}

	public String getTweetText() {
		return tweetText;
	}

	public TweetAnalysis setTweetText(String tweetText) {
		this.tweetText = tweetText;
		return this;
	}

	public Integer getScore() {
		return score;
	}

	public TweetAnalysis setScore(Integer score) {
		this.score = score;
		return this;
	}

	public Vector getProbabilities() {
		return probabilities;
	}

	public TweetAnalysis setProbabilities(Vector probabilities) {
		this.probabilities = probabilities;
		return this;
	}

	public Map<String, Integer> getCounts() {
		return counts;
	}

	public TweetAnalysis setCounts(Map<String, Integer> counts) {
		this.counts = counts;
		return this;
	}
	
	
}
