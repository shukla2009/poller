package com.avalia.async.poller

/**
  * Created by rahul on 26/5/18.
  */

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import akka.actor.{Actor, ActorLogging, Props}
import com.avalia.async.poller.Collector.{CleanFilter, News}

object Collector {

  case class News(source: String, title: String)

  case object CleanFilter

  def props = Props[Collector]

}

class Collector extends Actor with ActorLogging {

  var deDupFilter = scala.collection.immutable.Map[String, LocalDateTime]()

  override def receive: Receive = {
    case News(s, t) => news(s, t)
    case CleanFilter => cleanFilter();
  }

  /**
    * Clean dedup cache for keys older the 24 hours
    */
  def cleanFilter() = {
    val now = LocalDateTime.now()
    deDupFilter = deDupFilter.filter(r => {
      val diff = r._2.until(now, ChronoUnit.HOURS)
      diff < 24
    })
  }


  /**
    * accept news message and check for dedup
    *
    * @param s Source Id
    * @param t News title
    */
  private def news(s: String, t: String) = {
    deDupFilter.get(t) match {
      case Some(value) => deDupFilter += (t -> LocalDateTime.now())
      case None => deDupFilter += (t -> LocalDateTime.now())
        println(s, t);
    }
  }
}
