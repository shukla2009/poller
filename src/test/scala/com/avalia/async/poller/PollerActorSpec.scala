//#full-example
package com.avalia.async.poller

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.language.postfixOps

class PollerActorSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("async-poller"))

  override def afterAll: Unit = {
    shutdown(system)
  }

//    "A Poller Actor" should {
//      "should return Send " in {
//        val testProbe = TestProbe()
//        val helloGreetingMessage = "hello"
//        val helloGreeter = system.actorOf(Greeter.props(helloGreetingMessage, testProbe.ref))
//        val greetPerson = "Akka"
//        helloGreeter ! WhoToGreet(greetPerson)
//        helloGreeter ! Greet
//        testProbe.expectMsg(500 millis, Greeting(helloGreetingMessage + ", " + greetPerson))
//      }
//    }
}

