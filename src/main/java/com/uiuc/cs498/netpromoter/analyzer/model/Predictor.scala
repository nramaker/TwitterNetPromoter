package com.uiuc.cs498.netpromoter.analyzer.model

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.mllib.linalg.Vector
import scala.collection.JavaConverters._

import com.uiuc.cs498.netpromoter.analyzer.TweetParser;
import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetAnalysis
import com.uiuc.cs498.netpromoter.analyzer.busobj.TextAnalysis
import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetScore
import com.uiuc.cs498.netpromoter.analyzer.WordCounter

import java.util.HashMap;
import java.util.Map;

class Predictor(context: SparkContext, method: String) {
  val ModelPath = "E:/UIUC/CS498/Project/myModel"
  
  var model : NaiveBayesModel = null
  var predictionMethod = method;
  
  def trainModel(path: String, separator: String): Unit = {
   println("Starting initialization")

     WordCounter.clear()
     
     if(predictionMethod!="stanford"){
       if(separator.equalsIgnoreCase("\t"))
         model = ModelTrainer.trainTSV(path, context)
       else
         model = ModelTrainer.trainCSV(path, context)
       println("NaiveBeyes Initialization complete")
     }
     else
     {
       println("StanfordCoreNLP Initialization complete")
     }
  }
  
  def scoreTweets(tweets: java.util.List[String]): TweetScore = {
    println("Classifying Multiple Tweets")
    
    var score = new TweetScore()
    val scalaTweets = tweets.asScala.toSet
    scalaTweets.foreach { tweet =>  
      var analysis = classifySentiment(tweet)
      score.addTweet(analysis)
    }
    score
  }
  
  def classifySentiment(tweetText: String):  TweetAnalysis = {
     println("Classifying tweet: "+tweetText)
     val cleanedTweetTokens = TweetParser.cleanAndTokenizeTweet(tweetText, true)
     val featuresVector = TweetParser.generateWordCountVector(cleanedTweetTokens)

     if(predictionMethod!="stanford"){
       var probabilitiesVector = model.predictProbabilities(featuresVector)
     val naiveBeyesScore = gradeSentiment(probabilitiesVector);
     println("NaiveBeyes score = "+naiveBeyesScore)
     println("")
     
     var analysis = new TweetAnalysis()
     analysis.tweetText(tweetText)
       .score(naiveBeyesScore)
       .probabilities(probabilitiesVector)
     }
     else{
       var stanfordNLPScore = StanfordNLPScorer.scoreTweet(tweetText)
       println("Stanford score = "+stanfordNLPScore)
       println("")
       var analysis = new TweetAnalysis()
     analysis.tweetText(tweetText)
       .score(stanfordNLPScore)
     }
  }
  
    def classifyText(text: String):  TextAnalysis = {
     println("Classifying text: "+text)
//     val cleanedTweetTokens = TweetParser.cleanAndTokenizeTweet(text, true)
     //val featuresVector = TweetParser.generateWordCountVector(cleanedTweetTokens)

     
     val tuple = StanfordNLPScorer.scoreText(text)
     var stanfordNLPScore = tuple._1
     var map : HashMap[String, Double] = tuple._2
     
     println("Stanford score = "+stanfordNLPScore)
     println("")
     var analysis = new TextAnalysis()
     analysis.text(text)
       .score(stanfordNLPScore)
       .setCounts(map)
  }
  
  private[this] def gradeSentiment(probabilities: Vector): Integer = {
    val neg = probabilities(0)
    val mid = probabilities(1)
    val pos = probabilities(2)

    if(neg > 0.5)
    {
      if(neg>0.90)
        return 1
      if(neg>0.80)
        return 2
      if(neg>0.70)
        return 3
      if(neg>0.6)
        return 4
      return 5
    }
    else if(pos > 0.5)
    {
      if(pos>0.90)
        return 10
      if(pos>0.80)
        return 9
      return 8
    }
    else
    {
      if(mid > 0.6)
        return 7
      return 6
    }
  }
}