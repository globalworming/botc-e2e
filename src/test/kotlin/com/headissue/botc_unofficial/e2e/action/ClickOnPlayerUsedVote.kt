package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.page.Grimoire
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.PerformOn

open class ClickOnPlayerUsedVote(val name: String) : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.attemptsTo(PerformOn.eachMatching(Grimoire.player, fun(it: WebElementFacade) {
      if (it.textContent.contains(name)) {
        it.thenFind<WebElementFacade>(".canVote input").click()
      }
    }))
  }
}