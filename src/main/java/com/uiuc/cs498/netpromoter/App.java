package com.uiuc.cs498.netpromoter;

import spark.Request;
import spark.Response;
import spark.Route;
 
import static spark.Spark.*;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

import com.google.gson.Gson;
import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetAnalysis;
import com.uiuc.cs498.netpromoter.analyzer.model.Predictor;

public class App 
{
	
	public static final String MOVIE_REVIEW_DATA= "resources/data/movie_reviews/train.tsv";
	public static final String TAB_DELIMITER = "\t";
	
	
    public static void main(String[] args) {
        get("/", (request, response) -> "Hello World");
        
        get("/score", (request, response) -> {
        	String tweetSubject = request.queryParams("tweetSubject");
        	return "Someday I'll score your tweets of "+tweetSubject;
        });
        get("/classify", (request, response) -> {
        	
        	String tweetText = request.queryParams("tweetText");
        	if(tweetText==null)
        		return "Empty tweet!";
        	
        	Gson gson = new Gson();
        	
        	SparkConf sparkConf = new SparkConf().setAppName("TweetNetPromoter")
        			.setMaster("local[*]")
        			.set("spark.sql.warehouse.dir", "file:///E:/UIUC/CS498/TweetNetPromoter/")
        			.set("spark.executor.memory","1g");
        	SparkContext sparkContext = new SparkContext(sparkConf);
            
        	Predictor predictor = new Predictor(sparkContext);
        	predictor.trainModel(MOVIE_REVIEW_DATA, TAB_DELIMITER);
            
            TweetAnalysis analysis = predictor.classifySentiment(tweetText);
            return gson.toJson(analysis, TweetAnalysis.class);
        });
    }
}
