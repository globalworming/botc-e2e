package com.headissue.botc.e2e

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Open
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible
import net.serenitybdd.screenplay.questions.WebElementQuestion.the

open class SetUpNewGameTable : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.attemptsTo(Open.browserOn(Root()))
    actor.should(eventually(seeThat(
        the(".App"), isVisible()))
        .orComplainWith(CantAccessFrontend::class.java, "local fe running?"))
    actor.attemptsTo(Open.browserOn(DebugGameTable()))
    actor.should(eventually(seeThat(the(".grimoire"), isVisible())))
    actor.should(eventually(seeThat(the(".noPlayers"), isVisible())))
  }

}
