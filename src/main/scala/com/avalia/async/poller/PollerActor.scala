package com.avalia.async.poller

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.avalia.async.poller.Collector.News
import com.avalia.async.poller.Poller.Poll
import spray.json._

/**
  * Created by rahul on 26/5/18.
  */

object Poller {

  case object Poll

  def props(source: String, collector: ActorRef, key: String): Props = Props(new Poller(source, collector, key))
}

case class Response(status: String = "", totalResults: Int, articles: List[Article] = List.empty)

case class Article(source: Source, author: String = "", title: String = "", description: String = "", url: String = "", publishedAt: String = "")

case class Source(id: String, name: String)

class Poller(source: String, collector: ActorRef, key: String) extends Actor with ActorLogging with JsonSupport {
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = this.context.system.dispatcher
  val http = Http(this.context.system)

  override def receive() = {
    case Poll =>
      http
        .singleRequest(HttpRequest(uri = s"https://newsapi.org/v2/top-headlines?sources=$source&apiKey=$key"))
        .pipeTo(self)
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response" + body.utf8String)
        val response = body.utf8String.parseJson.convertTo[Response]
        response.articles.foreach(a => collector ! News(a.source.id, a.title))
      }
    case resp@HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
  }


}
