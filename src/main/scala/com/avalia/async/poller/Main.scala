//#full-example
package com.avalia.async.poller

import akka.actor.ActorSystem
import com.typesafe.config._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Main extends App {
  private val config = ConfigFactory.load()
  private val sources = config.getStringList("app.sources").asScala
  val system: ActorSystem = ActorSystem("async-poller")
  private val collector = system.actorOf(Collector.props)
  sources.foreach(s => {
    val poller = system.actorOf(Poller.props(s, collector, config.getString("app.key")), s)
    system.scheduler.schedule(0 seconds, config.getInt("app.timer.poller") seconds, poller, Poller.Poll)
  })
  system.scheduler.schedule(0 seconds, config.getInt("app.timer.cache") hours, collector, Collector.CleanFilter)
}
