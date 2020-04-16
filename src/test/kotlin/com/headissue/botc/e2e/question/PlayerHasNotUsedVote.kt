package com.headissue.botc.e2e.question

import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.annotations.Subject

class PlayerHasNotUsedVote(val name: String) : QuestionWithDefaultSubject<Boolean>() {

  @Subject("player '#name' can still vote")
  override fun answeredBy(actor: Actor): Boolean {
    return actor.asksFor(Player(name)).thenFind<WebElementFacade>(".canVote").isVisible
  }

}
