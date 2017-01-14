package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.actor.Actor.Receive
import akka.event.Logging

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


  override def receive: Receive = {
    case msg: String =>
      log.info("received...")
      log.info(msg)
      wsOut ! ("I received your message: " + msg)
  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }
}
