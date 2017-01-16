package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.controller.CreateUpdateUI
import play.api.libs.json.{JsValue, Json}
import Util.Utils._

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


  override def receive: Receive = {

    // message from controller
    case update: CreateUpdateUI => wsOut ! Json.toJson("HI")


    case json: JsValue => json match {

        // TODO: distinguish json messages
      case unknownMessage@_ => {
        log.info(s"unknown message: $unknownMessage")
        wsOut ! Json.toJson(unknownMessage)
      }
    }
  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }
}
