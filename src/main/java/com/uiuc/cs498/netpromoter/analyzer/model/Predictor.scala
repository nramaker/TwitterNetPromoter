package com.uiuc.cs498.netpromoter.analyzer.model

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.mllib.linalg.Vector
import scala.collection.JavaConverters._

 import com.uiuc.cs498.netpromoter.analyzer.TweetParser;
 import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetAnalysis;
 import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetScore;

class Predictor(context: SparkContext) {
  val ModelPath = "E:/UIUC/CS498/Project/myModel"
  
  var model : NaiveBayesModel = null
  
  def trainModel(path: String, separator: String): Unit = {
   println("Starting initialization")

   if(separator.equalsIgnoreCase("\t"))
     model = ModelTrainer.trainTSV(path, context)
   else
     model = ModelTrainer.trainCSV(path, context)
   println("Initialization complete")
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
//     println("Probability[negative, neutral, positive]")
     val cleanedTweetTokens = TweetParser.cleanAndTokenizeTweet(tweetText)
     val featuresVector = TweetParser.generateWordCountVector(cleanedTweetTokens)

     var probabilitiesVector = model.predictProbabilities(featuresVector)
//     println(probabilitiesVector)
     
     var analysis = new TweetAnalysis()
     analysis.tweetText(tweetText)
       .score(gradeSentiment(probabilitiesVector))
       .probabilities(probabilitiesVector)
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