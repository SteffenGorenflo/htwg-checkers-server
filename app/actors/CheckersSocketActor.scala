package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.controller.RegisterUI
import de.htwg.se.checkers.model._
import de.htwg.se.checkers.model.api.Coord
import de.htwg.se.checkers.model.enumeration.Colour
import play.api.libs.json.{JsObject, JsValue, Json, Writes}

/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor with ActorLogging {

  checkersController ! RegisterUI

  override def preStart: Unit = {
    log.info("Starting")
  }


  override def receive: Receive = {

    // messages from controller
    case currentPlayer: Colour.Value => wsOut ! Json.toJson(currentPlayer)
    case possibleMoves: Moves => wsOut ! Json.toJson(possibleMoves)
    case possiblePieces: Origins => wsOut ! Json.toJson(possiblePieces)
    case possibleTargets: Targets => wsOut ! Json.toJson(possibleTargets)
    case update: GameState => wsOut ! Json.toJson(transform(update))

    // message from websocket
    case json: JsValue => json match {


      // TODO: distinguish json messages
      case unknownMessage@_ =>
        log.info(s"unknown message: $unknownMessage")
        wsOut ! Json.toJson(unknownMessage)
    }

    case default@_ => log.info(s"unkown message: $default from " + sender())
  }


  override def postStop: Unit = {
    // TODO:
    // someResource.close()
  }


  /**
    * Helper class for transforming state class for a json conform case class
    *
    * @param update
    * @return
    */
  def transform(update: GameState): GameStateJson = {
    val coords: Vector[(Coord, Piece)] = update.field.board.zipWithIndex flatMap {
      o =>
        o._1.zipWithIndex.map {
          i => ((o._2, i._2), i._1)
        } collect {
          case (t, Some(p)) => (t, p)
        }
    }

    GameStateJson(coords, update.currentPlayer)
  }


  implicit val jsonPositionWrites = new Writes[Coord] {
    def writes(position: Coord): JsObject = Json.obj(
      "x" -> position._1,
      "y" -> position._2
    )
  }

  implicit val pieceFormat = Json.format[Piece]

  implicit val posPieceWrites = new Writes[(Coord, Piece)] {
    def writes(posPiece: (Coord, Piece)): JsObject = Json.obj(
      "pos" -> Json.toJson(posPiece._1),
      "piece" -> Json.toJson(posPiece._2)
    )
  }

  implicit val jsonPossibleMoves = new Writes[(Coord, Coord)] {
    def writes(piecePiece: (Coord, Coord)): JsObject = Json.obj(
      "origin" -> Json.toJson(piecePiece._1),
      "target" -> Json.toJson(piecePiece._2)
    )
  }

  implicit val coordWrites = new Writes[Seq[(Coord, Piece)]] {
    override def writes(o: Seq[(Coord, Piece)]): JsValue = Json.toJson(o)
  }

  implicit val moveWrites = new Writes[Seq[(Coord, Coord)]] {
    override def writes(o: Seq[(Coord, Coord)]): JsValue = Json.toJson(o)
  }


  implicit val updateWrite = Json.writes[GameStateJson]
  implicit val movesWrite = Json.writes[Moves]
  implicit val targetsWrite = Json.writes[Targets]
  implicit val originsWrite = Json.writes[Origins]
}
