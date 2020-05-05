package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.model.Player
import net.serenitybdd.screenplay.Actor
import java.util.*
import kotlin.NoSuchElementException

class ThePlayer(val name: String) : QuestionWithDefaultSubject<Player>() {
  override fun answeredBy(actor: Actor): Player {
    val players = actor.asksFor(PlayersAtTable())
    val player = Optional.ofNullable(players.find { it.name == name })
    return player.orElseThrow { NoSuchElementException() }
  }

}
