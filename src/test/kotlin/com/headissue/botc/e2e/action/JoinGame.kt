package com.headissue.botc.e2e.action

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Enter
import org.openqa.selenium.Keys

open class JoinGame : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.attemptsTo(Enter.theValue(actor.name).into(".mocks .addPlayer").thenHit(Keys.ENTER))
  }

}
