package src.main.scala.de.htwg.se.checkers.model

import src.main.scala.de.htwg.se.checkers.model.enumeration.Direction
import src.main.scala.de.htwg.se.checkers.model.api.Coord

package object api {
  type Coord = (Int, Int)
  type CoordStep = ((Int, Int), Boolean)
  type Move = ((Int, Int), (Int, Int))
  type MoveCheck = ((Int, Int), (Int, Int), Boolean)

}

object CoordUtil {

  implicit class BetterCoord(val c: Coord) {

    def oneStepRight(piece: Piece, direction: Direction.Value): Coord = {
      if (direction.equals(Direction.DOWN)) (c._1 + 1, c._2 + 1) else (c._1 + 1, c._2 - 1)
    }

    def oneStepLeft(piece: Piece, direction: Direction.Value): Coord = {
      if (direction.equals(Direction.DOWN)) (c._1 - 1, c._2 + 1) else (c._1 - 1, c._2 - 1)
    }
  }

}
