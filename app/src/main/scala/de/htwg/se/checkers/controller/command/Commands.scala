package src.main.scala.de.htwg.se.checkers.controller.command

import src.main.scala.de.htwg.se.checkers.model.api.Coord

trait Command {}

case class SetPiece(start: Coord, end: Coord) extends Command

case class QuitGame() extends Command

case class NewGame() extends Command

case class GameStatus() extends Command

case class GetMoves() extends Command

case class GetCurrentPlayer() extends Command

case class GetPossiblePieces() extends Command

case class GetPossibleTargets(coord: Coord) extends Command