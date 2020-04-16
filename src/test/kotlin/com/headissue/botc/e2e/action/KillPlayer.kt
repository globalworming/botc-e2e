package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.question.Player
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.thucydides.core.annotations.Step

open class KillPlayer(private val name: String) : Performable {

  @Step("{0} kill player named '#name'")
  override fun <T : Actor> performAs(actor: T) {
    actor.asksFor(Player(name))
        .thenFind<WebElementFacade>(".dead input")
        .click()
  }

}
