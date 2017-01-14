package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.controller.CreateUpdateUI
import play.api.libs.json.Json

/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor with ActorLogging {

  override def preStart() = {
    log.debug("Starting")
  }


  override def receive = {

    // message from controller
    case update: CreateUpdateUI =>

    case msg: String =>
      log.info("received...")
      log.info(msg)
      wsOut ! ("I received your message: " + msg)

    case unknownMessage@_ => {
      println(s"unknown message: $unknownMessage")
      wsOut ! Json.toJson(unknownMessage)
    }
  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }
}
