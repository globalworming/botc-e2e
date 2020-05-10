package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.Visibility
import net.serenitybdd.screenplay.rest.questions.LastResponse
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence
import org.hamcrest.CoreMatchers

class EvilWon : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return Visibility.of(".evilWon").viewedBy(actor).asBoolean()
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return LastResponse.received().answeredBy(actor).body().jsonPath().get("evilWon")
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
