package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.actor.Memories
import com.headissue.botc.e2e.page.GameTable
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.rest.interactions.Post
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence
import net.thucydides.core.annotations.Step
import org.apache.http.HttpStatus
import java.util.*
import kotlin.NoSuchElementException

open class KillPlayer(private val name: String) : Performable {

  @Step("{0} kill player named '#name'")
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      return Optional.ofNullable(GameTable.grimoire.player.resolveAllFor(actor).find { it.text.contains(name) })
          .orElseThrow { NoSuchElementException() }
          .thenFind<WebElementFacade>(".dead input").click()
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(Post.to("/gameTable/${tableName}/player/${name}/kill"))
      return actor.should(ResponseConsequence.seeThatResponse { it.statusCode(HttpStatus.SC_OK) })
    }

    throw NoMatchingAbilityException(actor.name)

  }

}
