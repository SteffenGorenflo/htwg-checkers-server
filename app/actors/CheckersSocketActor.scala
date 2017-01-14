package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.controller.CreateUpdateUI
import play.api.libs.json.{JsValue, Json}

/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor with ActorLogging {

  override def preStart: Unit = {
    log.debug("Starting")
  }


  override def receive = {

    // message from controller
    case update: CreateUpdateUI => wsOut ! Json.toJson(update)


    case json: JsValue => json match {
      case unknownMessage@_ => {
        println(s"unknown message: $unknownMessage")
        wsOut ! Json.toJson(unknownMessage)
      }
    }
    case default => wsOut ! ("I dont know what to do with: " + default)
  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }
}
