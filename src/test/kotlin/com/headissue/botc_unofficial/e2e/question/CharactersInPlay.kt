package com.headissue.botc_unofficial.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.targets.Target
import kotlin.streams.toList

class CharactersInPlay : QuestionWithDefaultSubject<List<String>>() {
  override fun answeredBy(actor: Actor): List<String> =
      actor.asksFor(PlayersAtTable()).map { it.character }.toList()

}
