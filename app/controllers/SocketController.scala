package controllers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import actors.CheckersSocketActor
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import play.api.mvc._
import play.api.libs.streams._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by steffen on 14/01/2017.
  */
class SocketController @Inject()(implicit system: ActorSystem, materializer: Materializer) extends Controller {

  def socket: WebSocket = WebSocket.accept[String, String] { request =>

    val actorPath = "" // TODO:

    //    // let the system retrieve the controller actor
    //    val selection = system.actorSelection(actorPath)
    //    implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)
    //    val controller = Await.result(selection.resolveOne(), Duration.Inf)

    println("received:")
    println(request)
    val controller1 = Props[MyActor]
    val controller = system.actorOf(controller1)

    ActorFlow.actorRef(out => CheckersSocketActor.props(out, controller))
  }


}
