package com.uiuc.cs498.netpromoter;

import spark.Request;
import spark.Response;
import spark.Route;
 
import static spark.Spark.*;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkContext;

import com.google.gson.Gson;
import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetAnalysis;
import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetScore;
import com.uiuc.cs498.netpromoter.analyzer.SparkContextGenerator;
import com.uiuc.cs498.netpromoter.analyzer.model.Predictor;

public class App 
{
	
	public static final String MOVIE_REVIEW_DATA= "resources/data/movie_reviews/train.tsv";
	public static final String TAB_DELIMITER = "\t";
	
	
    public static void main(String[] args) {
        get("/", (request, response) -> "Hello World");
        
        get("/score", (request, response) -> {
        	String tweetSubject = request.queryParams("tweetSubject");
        	
        	Gson gson = new Gson();
        	
        	SparkContext sparkContext =SparkContextGenerator.getContextInstance();
        	Predictor predictor = new Predictor(sparkContext);
        	predictor.trainModel(MOVIE_REVIEW_DATA, TAB_DELIMITER);
        	
        	//TODO: getActual tweets from Twitter
        	List<String> fakeTweets = Arrays.asList(
        		"happy great awesome",
        		"happy great awesome fantastic",
        		"happy great awesome pretty",
        		"happy great awesome happy",
        		"happy great awesome kinda",
           		"happy great pretty",
        		"happy awesome happy",
        		"great awesome kinda",
        		"meh okay bland",
        		"whatever I guess",
        		"awful hate terrible",
        		"awful hate sucks",
        		"awful terrible sucks",
        		"hate terrible sucks"
        	);
        	TweetScore score = predictor.scoreTweets(fakeTweets);
        	return gson.toJson(score);
        	//return "Someday I'll score your tweets of "+tweetSubject;
        });
        get("/classify", (request, response) -> {
        	
        	String tweetText = request.queryParams("tweetText");
        	if(tweetText==null)
        		return "Empty tweet!";
        	
        	Gson gson = new Gson();
        	
        	SparkContext sparkContext =SparkContextGenerator.getContextInstance();
            
        	Predictor predictor = new Predictor(sparkContext);
        	predictor.trainModel(MOVIE_REVIEW_DATA, TAB_DELIMITER);
            
            TweetAnalysis analysis = predictor.classifySentiment(tweetText);
            return gson.toJson(analysis, TweetAnalysis.class);
        });
    }
}
