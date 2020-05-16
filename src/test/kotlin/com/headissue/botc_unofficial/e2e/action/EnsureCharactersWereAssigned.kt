package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalIntegratedFrontend
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.question.CharactersInPlay
import com.headissue.botc_unofficial.e2e.question.DistinctCharacters
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.GivenWhenThen.*
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsIterableContainingInOrder

open class EnsureCharactersWereAssigned : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      return actor.should(seeThat(CharactersInPlay(), IsIterableContainingInOrder.contains(
          "Slayer", "Librarian", "Spy", "Imp", "Empath"
      )))
    }

    if (actor.abilityTo(AccessLocalIntegratedFrontend::class.java) != null) {
      return actor.should(seeThat(DistinctCharacters(), `is`(true)))
    }




    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
