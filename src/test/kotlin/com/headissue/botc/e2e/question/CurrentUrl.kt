package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.abilities.BrowseTheWeb

class CurrentUrl : QuestionWithDefaultSubject<String>() {
  override fun answeredBy(actor: Actor): String = BrowseTheWeb.`as`(actor).driver.currentUrl

}
