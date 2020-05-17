package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalIntegratedFrontend
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.page.TownSquare
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Enter
import org.openqa.selenium.Keys

open class NominatePlayer(val name: String) : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      actor.attemptsTo(Enter.theValue(name).into(".playerToNominate"))
      actor.attemptsTo(Enter.theValue(actor.name).into(".nominatingPlayer").thenHit(Keys.ENTER))
      return
    }

    if (actor.abilityTo(AccessLocalIntegratedFrontend::class.java) != null) {
      TownSquare.player.resolveAllFor(actor).first { it.containsText(name) }
          .thenFind<WebElementFacade>(".nominate")
          .click()
      return
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
