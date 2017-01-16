package Util

import de.htwg.se.checkers.controller.CreateUpdateUI
import play.libs.Json


object Utils {

  implicit class CheckerJson(json: Json) {
    def writes(update: CreateUpdateUI) = ???
  }

}
