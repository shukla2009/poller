package com.avalia.async.poller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport


/**
  * Created by rahul on 26/5/18.
  */
trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val sourceJsonFormat = jsonFormat2(Source)
  implicit val articleJsonFormat = jsonFormat6(Article)
  implicit val responseJsonFormat = jsonFormat3(Response)

}
