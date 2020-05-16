package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.Presence
import kotlin.test.assertTrue

class DistinctCharacters : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    val characters = actor.asksFor(PlayersAtTable()).map { it.character }.toList()
    assertTrue { characters.isNotEmpty() }
    return characters.distinct().size == characters.size
  }
}
