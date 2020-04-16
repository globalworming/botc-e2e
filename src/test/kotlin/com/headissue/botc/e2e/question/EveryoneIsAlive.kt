package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible
import net.serenitybdd.screenplay.questions.WebElementQuestion

class EveryoneIsAlive : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    actor.should(eventually(seeThat(WebElementQuestion.the(".townsquare .dead"), isNotVisible())))
    return true
  }

}
