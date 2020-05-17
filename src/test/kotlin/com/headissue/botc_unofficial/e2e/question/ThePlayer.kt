package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.model.Player
import net.serenitybdd.screenplay.Actor

class ThePlayer(val name: String) : QuestionWithDefaultSubject<Player>() {
  override fun answeredBy(actor: Actor): Player {
    val players = actor.asksFor(PlayersAtTable())
    return players.first { it.name == name }
  }

}
