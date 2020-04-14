package com.headissue.botc.e2e

import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Question
import net.serenitybdd.screenplay.targets.Target

class PlayersAtTable : Question<List<WebElementFacade>> {
  override fun answeredBy(actor: Actor): List<WebElementFacade> =
    Target.the("displayed players").locatedBy(".player").resolveAllFor(actor)


}
