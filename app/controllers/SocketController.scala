package controllers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import actors.CheckersSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by steffen on 14/01/2017.
  */
class SocketController @Inject()(implicit system: ActorSystem, materializer: Materializer) extends Controller {

  def socket: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>

    val actorPath = "akka.tcp://checkers@127.0.0.1:2552/user/controller"

    // let the system retrieve the controller actor
    val selection = system.actorSelection(actorPath)
    implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)
    val controller = Await.result(selection.resolveOne(), Duration.Inf)


    ActorFlow.actorRef(out => CheckersSocketActor.props(out, controller))
  }


}
