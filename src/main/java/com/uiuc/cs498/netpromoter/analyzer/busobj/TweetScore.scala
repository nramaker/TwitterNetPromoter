package com.uiuc.cs498.netpromoter.analyzer.busobj

class TweetScore() {
  
  var score: Double = 0
  var posCount: Integer = 0
  var negCount: Integer = 0
  var neutralCount: Integer = 0
  
  var analyzedTweets : java.util.List[TweetAnalysis] = new java.util.ArrayList[TweetAnalysis]()
  var topWords : java.util.Map[String, Integer] = new java.util.HashMap[String, Integer]()
  
  def getScore(): Double = {
    this.score
  }
  
  def getPosCount(): Integer = {
    this.posCount
  }
  
  def getNegCount(): Integer = {
    this.negCount
  }
  
   def getNeutralCount(): Integer = {
    this.neutralCount
  }
   
  def getTweets(): java.util.List[TweetAnalysis] = {
    this.analyzedTweets
  }
  
  def getTopWords(): java.util.Map[String, Integer] = {
    
    this.topWords
  }
  
  def addTweet(tweet: TweetAnalysis): TweetScore = {
    if(tweet.score >= 8)
      this.posCount+=1
    else if(tweet.score <= 5)
      this.negCount+=1
    else
      this.neutralCount+=1
      
    this.analyzedTweets.add(tweet)
    
    calcWordCounts()
    calcScore()
  }
  
  def calcScore() : TweetScore = {
    val count: Integer = posCount+negCount+neutralCount
    
    val percentPos : Double = (posCount.doubleValue()/count) * 100.0
    val percentNeg : Double = (negCount.doubleValue()/count) * 100.0
    
    this.score = (percentPos - percentNeg)
    this
  }
  
  def calcWordCounts(): TweetScore = {
        //TODO: process top words
    this.topWords.put("UIUC", new Integer(90))
    this.topWords.put("MSC-DS", new Integer(65))
    this.topWords.put("Jasdeep", new Integer(60))
    this.topWords.put("Duggal", new Integer(60))
    this.topWords.put("Nathan", new Integer(60))
    this.topWords.put("Ramaker", new Integer(60))
    this.topWords.put("CS498", new Integer(55))
    this.topWords.put("machine", new Integer(40))
    this.topWords.put("learning", new Integer(35))
    this.topWords.put("awesome",new Integer(30))
    this.topWords.put("wonderful",new Integer(20))
    this.topWords.put("data",new Integer(10))
    this.topWords.put("science",new Integer(8))
    
    this
  }
  
  
}