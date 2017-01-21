package src.main.scala.de.htwg.se.checkers

import akka.actor.{ActorSystem, Props}
import src.main.scala.de.htwg.se.checkers.controller.ControllerActor

object Checkers {

  // inject
  implicit val bindingModule = CheckersConfiguration

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("checkers")
    val controllerActor = actorSystem.actorOf(Props(new ControllerActor()), "controller")

    // tui
    //actorSystem.actorOf(Props(new Tui(controllerActor)))
    //actorSystem.actorOf(Props(new Gui(controllerActor)))
  }
}
