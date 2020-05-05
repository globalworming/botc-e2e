package com.headissue.botc.e2e.question

import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.annotations.Subject

class PlayerHasUsedVote(val name: String) : QuestionWithDefaultSubject<Boolean>() {

  @Subject("player '#name' has used vote")
  override fun answeredBy(actor: Actor): Boolean {
    return actor.asksFor(ThePlayer(name)).thenFind<WebElementFacade>(".usedVote").isVisible
  }

}
