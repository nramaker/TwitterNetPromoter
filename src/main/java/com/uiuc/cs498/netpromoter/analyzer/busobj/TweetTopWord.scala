package com.uiuc.cs498.netpromoter.analyzer.busobj

class TweetTopWord(n: String, c: Int) {
  var name=n
  var count=c
  
  def name(n: String): TweetTopWord = {
    this.name=n
    this
  }
  
  def getName(): String = {
    this.name
  }
  
  def count(c: Int): TweetTopWord = {
    this.count=c
    this
  }
  
  def getCount(): Int = {
    this.count
  }
}