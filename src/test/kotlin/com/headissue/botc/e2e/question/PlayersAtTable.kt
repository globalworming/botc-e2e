package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.targets.Target

class PlayersAtTable : QuestionWithDefaultSubject<List<WebElementFacade>>() {
  override fun answeredBy(actor: Actor): List<WebElementFacade> {
    if (actor.abilityTo(SeeGrimoire::class.java) != null) {
      return Target.the("displayed players").locatedBy(".grimoire .player").resolveAllFor(actor)
    }

    if (actor.abilityTo(SeeTownSquare::class.java) != null) {
      return Target.the("displayed players").locatedBy(".grimoire .player").resolveAllFor(actor)
    }

    throw NoMatchingAbilityException(actor.name)
  }


}
