package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.Presence

class EveryoneIsAlive : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return !Presence.of(".townSquare .dead").viewedBy(actor).asBoolean()
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return actor.asksFor(PlayersAtTable()).filter { it.dead == true }.isEmpty()
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
