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
        	String method = request.queryParams("method");
        	if(method==null)
        		method = "stanford";
        	
        	Gson gson = new Gson();
        	
        	SparkContext sparkContext =SparkContextGenerator.getContextInstance();
        	Predictor predictor = new Predictor(sparkContext, method);
        	predictor.trainModel(MOVIE_REVIEW_DATA, TAB_DELIMITER);
        	
        	SearchTwitter twitter = new SearchTwitter(tweetSubject);
        	List<String> tweets = twitter.buildTweetsList();
        	
        	if(tweets==null || tweets.size()==0)
        		return "No tweets found!";
        	
        	TweetScore score = predictor.scoreTweets(tweets);
            score.calcWordCounts();
            score.calcScore();
        	return gson.toJson(score, TweetScore.class);
        });
//        get("/classify", (request, response) -> {
//        	
//        	String tweetText = request.queryParams("tweetText");
//        	if(tweetText==null)
//        		return "Empty tweet!";
//        	
//        	Gson gson = new Gson();
//        	
//        	SparkContext sparkContext =SparkContextGenerator.getContextInstance();
//            
//        	Predictor predictor = new Predictor(sparkContext);
//        	predictor.trainModel(MOVIE_REVIEW_DATA, TAB_DELIMITER);
//            
//            TweetAnalysis analysis = predictor.classifySentiment(tweetText);
//            return gson.toJson(analysis, TweetAnalysis.class);
//        });
    }
}
