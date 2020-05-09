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
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence
import org.apache.http.HttpStatus

open class StartNextDay : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      return actor.attemptsTo(Click.on(".startNextDay"))
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(Post.to("/gameTable/${tableName}/nextTurn"))
      return actor.should(ResponseConsequence.seeThatResponse { it.statusCode(HttpStatus.SC_OK) })
    }

    throw NoMatchingAbilityException(actor.name)

  }

}
