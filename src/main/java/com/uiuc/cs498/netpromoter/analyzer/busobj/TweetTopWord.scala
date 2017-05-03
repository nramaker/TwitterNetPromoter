package com.uiuc.cs498.netpromoter.analyzer.busobj

class TweetTopWord(n: String, c: Int) {
  var text=n
  var weight=c
  
  def text(n: String): TweetTopWord = {
    this.text=n
    this
  }
  
  def gettext(): String = {
    this.text
  }
  
  def weight(c: Int): TweetTopWord = {
    this.weight=c
    this
  }
  
  def getweight(): Int = {
    this.weight
  }
}