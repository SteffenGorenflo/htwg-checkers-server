package src.main.scala.de.htwg.se.checkers

import com.escalatesoft.subcut.inject._

object BindingKeys {
  object PlayfieldSize extends BindingId
  object NumberOfPlayableRows extends BindingId
}

object CheckersConfiguration extends NewBindingModule(module => {
  import BindingKeys._
  import module._

  bind[Int] idBy PlayfieldSize toSingle 8
  bind[Int] idBy NumberOfPlayableRows toSingle 3

})
