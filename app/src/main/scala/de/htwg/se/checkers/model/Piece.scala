package src.main.scala.de.htwg.se.checkers.model

import src.main.scala.de.htwg.se.checkers.model.enumeration.Colour

case class Piece(colour: Colour.Value, checkers: Boolean) {

  def this(color: Colour.Value) = this(color, false)

}
