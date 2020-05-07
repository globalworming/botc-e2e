package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.actor.Memories
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Enter
import net.serenitybdd.screenplay.rest.interactions.Post
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse
import org.apache.http.HttpStatus.SC_OK
import org.openqa.selenium.Keys

open class JoinGame : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      return actor.attemptsTo(Enter.theValue(actor.name).into(".mocks .addPlayer").thenHit(Keys.ENTER))
    }

    val tableName = actor.recall<String>(Memories.TABLE_NAME)
    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      actor.attemptsTo(Post.to("/gameTable/${tableName}/players")
          .with { it.param("name", actor.name) })
      return actor.should(seeThatResponse { it.statusCode(SC_OK) })
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
