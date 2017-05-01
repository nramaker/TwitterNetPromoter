package com.uiuc.cs498.netpromoter.analyzer.model

import java.util.Properties
import scala.collection.JavaConverters._
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import com.uiuc.cs498.netpromoter.analyzer.TweetParser;


object StanfordNLPScorer {
  
  val stanfordPipeline: StanfordCoreNLP = {
    val properties = new Properties()
    properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment")
    new StanfordCoreNLP(properties);
  }
  
  def scoreTweet(tweetText: String): Int = {
    val cleanedTweet = TweetParser.cleanTweet(tweetText, true)
    val document: edu.stanford.nlp.pipeline.Annotation = new edu.stanford.nlp.pipeline.Annotation(cleanedTweet)
    stanfordPipeline.annotate(document)   
    var sentiment:Double = 0.0
    var counter:Int = 0
    var sentences: scala.collection.immutable.List[CoreMap] = (document.get(classOf[CoreAnnotations.SentencesAnnotation])).asScala.toList
    for (sentence <- sentences) {
      val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
      sentiment += RNNCoreAnnotations.getPredictedClass(tree)
      counter+=1
    }
   convertSentimentToScore(sentiment/counter)
   }
  
  def convertSentimentToScore(sentiment: Double): Int = {
    if(sentiment > 2.0)
      return 10
    if(sentiment == 2.0)
      return 5
    return 1
  }
}