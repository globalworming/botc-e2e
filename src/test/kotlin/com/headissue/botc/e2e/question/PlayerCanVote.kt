package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.annotations.Subject

class PlayerCanVote(val name: String) : QuestionWithDefaultSubject<Boolean>() {

  @Subject("player '#name' can still vote")
  override fun answeredBy(actor: Actor): Boolean {
    return actor.asksFor(ThePlayer(name)).canVote

  }

}
