package controllers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import actors.CheckersSocketActor
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._
import src.main.scala.de.htwg.se.checkers.controller.ControllerActor

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by steffen on 14/01/2017.
  */
class SocketController @Inject()(implicit system: ActorSystem, materializer: Materializer) extends Controller {

  val controllerActor = system.actorOf(Props(new ControllerActor()), "controller")

  def socket: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>

    implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)


    val actorPath = "/user/controller"
//system.actorSelection(actorPath);
    // let the system retrieve the controller actor
    val selection = system.actorSelection(actorPath)
    val controller = Await.result(selection.resolveOne(), Duration.Inf)


    ActorFlow.actorRef(out => CheckersSocketActor.props(out, controller))
  }


}
