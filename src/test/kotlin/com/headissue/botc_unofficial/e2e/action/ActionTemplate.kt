package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalIntegratedFrontend
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable

open class ActionTemplate : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      return
    }

    if (actor.abilityTo(AccessLocalIntegratedFrontend::class.java) != null) {
      return
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
