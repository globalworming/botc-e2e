package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.question.EveryoneIsAlive
import com.headissue.botc.e2e.question.ItIsNight
import com.headissue.botc.e2e.question.PlayersAtTable
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible
import net.serenitybdd.screenplay.questions.CountQuestion
import net.serenitybdd.screenplay.questions.WebElementQuestion.the
import org.hamcrest.CoreMatchers.`is`

open class EnsureInitialTownSquareIsDisplayed : Performable {
  override fun <T : Actor> performAs(actor: T) {
    actor.should(eventually(seeThat(the(".townSquare"), isVisible())))
    // TODO actor.should(eventually(seeThat(the(".townsquare .players.me"), isVisible())))
    actor.should(eventually(seeThat(CountQuestion(PlayersAtTable()), `is`(5))))
    actor.should(eventually(seeThat(EveryoneIsAlive(), `is`(true))))
    actor.should(eventually(seeThat(ItIsNight(), `is`(true))))
  }

}
