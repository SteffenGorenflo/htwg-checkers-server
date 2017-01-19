package actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import de.htwg.se.checkers.controller.{DeregisterUI, RegisterUI}
import de.htwg.se.checkers.model._
import de.htwg.se.checkers.model.api.Coord
import de.htwg.se.checkers.model.enumeration.Colour
import play.api.libs.json._
import akka.pattern.ask
import de.htwg.se.checkers.controller.command._

import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by steffen on 14/01/2017.
  */
object CheckersSocketActor {
  def props(out: ActorRef, controller: ActorRef): Props = Props(new CheckersSocketActor(out, controller))
}

class CheckersSocketActor(val wsOut: ActorRef, val checkersController: ActorRef) extends Actor with ActorLogging {

  checkersController ! RegisterUI
  implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)


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
    case json: JsValue => transform(json) match {

      case query: Query =>
        checkersController ? query.command onSuccess {
          case currentPlayer: Colour.Value => wsOut ! Json.toJson(currentPlayer)
          case possibleMoves: Moves => wsOut ! Json.toJson(possibleMoves)
          case possiblePieces: Origins => wsOut ! Json.toJson(possiblePieces)
          case possibleTargets: Targets => wsOut ! Json.toJson(possibleTargets)
          case update: GameState => wsOut ! Json.toJson(transform(update))
        }

      case execute: Execute =>
        checkersController ! execute.command

      case default =>
        log.info("Json from Websocket not recognised")
        wsOut ! json
    }

    case default@_ => log.info(s"unkown message: $default from " + sender())
  }


  override def postStop: Unit = {
    checkersController ! DeregisterUI
  }


  def transformQuery(query: String, json: JsValue): Any = {
    query.toLowerCase match {
      case "getmoves" => Query(GetMoves)
      case "getcurrentplayer" => Query(GetCurrentPlayer)
      case "getpossiblepieces" => Query(GetPossiblePieces)
      case "gamestatus" => Query(GameStatus)
      case "getpossibletargets" =>
        val x: Int = (json \ "x").validate[Int].getOrElse(-1)
        val y: Int = (json \ "y").validate[Int].getOrElse(-1)

        if (x < 0 || y < 0) json // error
        else Query(GetPossibleTargets(x, y))

      case default => json
    }

  }

  def transformExecute(execute: String, json: JsValue): Any = {
    execute.toLowerCase match {
      case "quitgame" => Execute(QuitGame)
      case "newgame" => Execute(NewGame)
      case "setpiece" =>
        val x1: Int = (json \ "x1").validate[Int].getOrElse(-1)
        val y1: Int = (json \ "y1").validate[Int].getOrElse(-1)
        val x2: Int = (json \ "x2").validate[Int].getOrElse(-1)
        val y2: Int = (json \ "y2").validate[Int].getOrElse(-1)

        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) json // error
        else Execute(SetPiece((x1, y1), (x2, y2)))

      case default => json
    }
  }

  def transform(json: JsValue): Any = {
    val execute: String = (json \ "execute").validate[String].getOrElse("")
    val query: String = (json \ "query").validate[String].getOrElse("")


    if (!execute.isEmpty && !query.isEmpty) {
      log.info("both parameter are not allowed")
      json
    } else if (!execute.isEmpty) {
      transformExecute(execute, json)
    } else if (!query.isEmpty) {
      transformQuery(query, json)
    } else {
      json
    }
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
