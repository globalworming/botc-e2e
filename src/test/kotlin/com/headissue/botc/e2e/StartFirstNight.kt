package com.headissue.botc.e2e

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Click

open class StartFirstNight : Performable {
  override fun <T : Actor> performAs(actor: T) {
      actor.attemptsTo(Click.on(".startGame"))
  }

}
