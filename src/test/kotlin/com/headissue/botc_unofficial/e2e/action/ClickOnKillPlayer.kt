package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.page.GameTable
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable

open class ClickOnKillPlayer(private val name: String) : Performable {
  override fun <T : Actor> performAs(actor: T) {
    return actor.asksFor {
      GameTable.grimoire.player.resolveAllFor(actor)
          .find { it.text.contains(name) } ?: throw NoSuchElementException()
    }.thenFind<WebElementFacade>(".dead input").click()
  }
}