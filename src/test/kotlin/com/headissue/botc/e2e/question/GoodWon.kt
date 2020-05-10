package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.Visibility

class GoodWon : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return Visibility.of(".goodWon").viewedBy(actor).asBoolean()
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      TODO()
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
