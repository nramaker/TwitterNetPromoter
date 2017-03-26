package com.uiuc.cs498.netpromoter.analyzer.model

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.mllib.linalg.Vector

 import com.uiuc.cs498.netpromoter.analyzer.TweetParser;
 import com.uiuc.cs498.netpromoter.analyzer.busobj.TweetAnalysis;

class Predictor(context: SparkContext) {
  val ModelPath = "E:/UIUC/CS498/Project/myModel"
  
  val trainer = new ModelTrainer()
  var model : NaiveBayesModel = null
  
  def trainModel(path: String, separator: String): Unit = {
   println("Starting initialization")

   if(separator.equalsIgnoreCase("\t"))
     model = trainer.trainTSV(path, context)
   else
     model = trainer.trainCSV(path, context)
   println("Initialization complete")
  }
  
  def classifySentiment(tweetText: String):  TweetAnalysis = {
     println("\nClassifying tweet: "+tweetText)
     println("Probability[negative, neutral, positive]")
     val cleanedTweetTokens = TweetParser.cleanAndTokenizeTweet(tweetText)
     val featuresVector = TweetParser.generateWordCountVector(cleanedTweetTokens)

     var probabilitiesVector = model.predictProbabilities(featuresVector)
     println(probabilitiesVector)
     
     var analysis = new TweetAnalysis()
     analysis.setTweetText(tweetText)
       .setScore(gradeSentiment(probabilitiesVector))
       .setProbabilities(probabilitiesVector)
     
//     (gradeSentiment(probabilitiesVector),  probabilitiesVector)
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