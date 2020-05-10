package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actions.Click

open class ActionTemplate : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
