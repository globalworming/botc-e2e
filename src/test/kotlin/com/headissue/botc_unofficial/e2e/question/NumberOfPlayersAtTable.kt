package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.page.GameTable
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.Presence

class NumberOfPlayersAtTable : QuestionWithDefaultSubject<Int>() {
  override fun answeredBy(actor: Actor): Int {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return GameTable.grimoire.player.resolveAllFor(actor).size
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return actor.asksFor(PlayersAtTable()).size
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
