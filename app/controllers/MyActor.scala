package controllers

import akka.actor.{Actor, ActorPath, ActorRef}
import akka.event.Logging

/**
  * Created by steffen on 14/01/2017.
  */
class MyActor extends Actor {
  val log = Logging(context.system, this)

  def receive: Receive = {
    case "test" => log.info("received test")
    case _      => log.info("received unknown message")
  }
}
