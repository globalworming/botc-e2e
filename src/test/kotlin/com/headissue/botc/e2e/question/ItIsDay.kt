package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.action.GetTableInfo
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.rest.questions.LastResponse
import net.serenitybdd.screenplay.targets.Target

class ItIsDay : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      return Target.the("day icon").locatedBy(".day").resolveFor(actor).isVisible
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      actor.attemptsTo(GetTableInfo())
      return actor.asksFor(LastResponse()).jsonPath().getBoolean("isDay")
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
