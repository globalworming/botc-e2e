package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.actor.Memories
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actions.Click
import net.serenitybdd.screenplay.rest.interactions.Post
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse
import org.apache.http.HttpStatus.SC_OK

open class StartFirstNight : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return actor.attemptsTo(Click.on(".startGame"))
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(Post.to("/gameTable/${tableName}/start"))
      return actor.should(seeThatResponse { it.statusCode(SC_OK) })
    }

    throw NoMatchingAbilityException(actor.name)

  }

}
