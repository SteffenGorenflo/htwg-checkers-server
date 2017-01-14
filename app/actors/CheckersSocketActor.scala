package actors

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive

/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: String =>
      wsOut ! ("I received your message: " + msg)
  }


  override def postStop: Unit = {
   // TODO:
    // someResource.close()
  }
}