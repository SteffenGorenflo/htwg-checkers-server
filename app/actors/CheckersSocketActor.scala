package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.model.GameState
import play.api.libs.json.{JsValue, Json}

/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor with ActorLogging {

  override def preStart: Unit = {
    log.info("Starting")
  }

  implicit val updateFormat = Json.format[GameState]

  override def receive: Receive = {

    // message from controller
    case update: GameState => wsOut ! Json.toJson(update)


    case json: JsValue => json match {

      // TODO: distinguish json messages
      case unknownMessage@_ => {
        log.info(s"unknown message: $unknownMessage")
        wsOut ! Json.toJson(unknownMessage)
      }
    }
    case default@_ => log.info(s"unkown message: $default from " + sender())

  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }
}
