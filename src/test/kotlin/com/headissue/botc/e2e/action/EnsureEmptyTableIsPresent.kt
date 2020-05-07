package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.actor.Memories
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible
import net.serenitybdd.screenplay.questions.WebElementQuestion
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse
import org.hamcrest.CoreMatchers.`is`

open class EnsureEmptyTableIsPresent : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return actor.should(
          eventually(seeThat(WebElementQuestion.the(".grimoire"), isVisible())),
          seeThat(WebElementQuestion.the(".noPlayers"), isVisible())
      )
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      actor.attemptsTo(GetTableInfo())
      actor.should(
          seeThatResponse { it.body("id", `is`(actor.recall(Memories.TABLE_NAME) as String)) },
          seeThatResponse { it.body("players.size()", `is`(0)) }
      )
      return

    }

    throw NoMatchingAbilityException(actor.name)
  }

}
