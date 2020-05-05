package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.question.ThePlayer
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.thucydides.core.annotations.Step

open class MarkPlayerUsedVote(private val name: String) : Performable {

  @Step("{0} marks player named '#name' has used vote")
  override fun <T : Actor> performAs(actor: T) {
    actor.asksFor(ThePlayer(name))
        .thenFind<WebElementFacade>(".usedVote input")
        .click()
  }

}
