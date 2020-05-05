package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.question.EveryoneIsAlive
import com.headissue.botc.e2e.question.ItIsNight
import com.headissue.botc.e2e.question.PlayersAtTable
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.questions.CountQuestion
import org.hamcrest.CoreMatchers.`is`

open class EnsureInitialTownSquareIsDisplayed : Performable {
  override fun <T : Actor> performAs(actor: T) = actor.should(
      seeThat(CountQuestion(PlayersAtTable()), `is`(5)),
      seeThat(EveryoneIsAlive(), `is`(true)),
      seeThat(ItIsNight(), `is`(true))
  )

}
