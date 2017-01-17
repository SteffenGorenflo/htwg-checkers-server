package actors

import de.htwg.se.checkers.model.Piece
import de.htwg.se.checkers.model.api.Coord
import de.htwg.se.checkers.model.enumeration.Colour

/**
  * Created by steffen on 16/01/2017.
  */
case class GameStateJson(vector: Seq[(Coord, Piece)], currentPlayer: Colour.Value)
