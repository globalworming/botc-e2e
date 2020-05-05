package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.annotations.Subject

class PlayerIsDead(val name: String) : QuestionWithDefaultSubject<Boolean>() {

  @Subject("player is dead: #name")
  override fun answeredBy(actor: Actor): Boolean {
    val player = actor.asksFor(ThePlayer(name))
    return player.dead
  }

}
