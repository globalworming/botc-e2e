package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.page.Grimoire
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb

class NumberOfPlayersAtTable : QuestionWithDefaultSubject<Int>() {
  override fun answeredBy(actor: Actor): Int {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return Grimoire.player.resolveAllFor(actor).size
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return actor.asksFor(PlayersAtTable()).size
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
