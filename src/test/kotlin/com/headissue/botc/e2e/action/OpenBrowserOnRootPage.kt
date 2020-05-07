package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.error.CantAccessFrontend
import com.headissue.botc.e2e.page.Root
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence
import net.serenitybdd.screenplay.GivenWhenThen
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Open
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers
import net.serenitybdd.screenplay.questions.WebElementQuestion

open class OpenBrowserOnRootPage : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.attemptsTo(Open.browserOn(Root()))
    actor.should(EventualConsequence.eventually(GivenWhenThen.seeThat(
        WebElementQuestion.the(".App"), WebElementStateMatchers.isVisible()))
        .orComplainWith(CantAccessFrontend::class.java, "local fe running?"))
  }

}
