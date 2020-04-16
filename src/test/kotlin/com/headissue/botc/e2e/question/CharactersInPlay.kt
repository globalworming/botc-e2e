package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.targets.Target
import kotlin.streams.toList

class CharactersInPlay : QuestionWithDefaultSubject<List<String>>() {
  override fun answeredBy(actor: Actor): List<String> =
      Target.the("assigned characters").locatedBy(".player .isCharacter")
          .resolveAllFor(actor).stream().map { it.value }.toList()

}
