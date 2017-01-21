package actors

import src.main.scala.de.htwg.se.checkers.model.Piece
import src.main.scala.de.htwg.se.checkers.model.api.Coord
import src.main.scala.de.htwg.se.checkers.model.enumeration.Colour

/**
  * Created by steffen on 16/01/2017.
  */
case class GameStateJson(vector: Seq[(Coord, Piece)], currentPlayer: Colour.Value)
case class Query(command: Any) {}
case class Execute(command: Any) {}
