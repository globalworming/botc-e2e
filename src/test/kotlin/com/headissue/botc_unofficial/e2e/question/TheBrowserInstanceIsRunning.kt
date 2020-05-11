package com.headissue.botc_unofficial.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.abilities.BrowseTheWeb

class TheBrowserInstanceIsRunning : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean = !BrowseTheWeb.`as`(actor).driver.toString().contains("uninitialised", ignoreCase = true)

}
