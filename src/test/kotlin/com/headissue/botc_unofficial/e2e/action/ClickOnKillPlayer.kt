package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.page.GameTable
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.PerformOn

open class ClickOnKillPlayer(val name: String) : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.attemptsTo(PerformOn.eachMatching(GameTable.grimoire.player, fun(it: WebElementFacade) {
      if (it.textContent.contains(name)) {
        it.thenFind<WebElementFacade>(".dead input").click()
      }
    }))
  }
}