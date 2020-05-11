package com.headissue.botc_unofficial.e2e.action

import com.headissue.botc_unofficial.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc_unofficial.e2e.ability.AccessLocalIntegratedFrontend
import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.actor.Memories
import com.headissue.botc_unofficial.e2e.page.DebugGameTable
import com.headissue.botc_unofficial.e2e.page.Root
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Enter
import net.serenitybdd.screenplay.actions.Open
import net.serenitybdd.screenplay.rest.interactions.Post
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse
import org.apache.http.HttpStatus.SC_OK
import org.openqa.selenium.Keys

open class SetUpNewGameTable : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      actor.attemptsTo(OpenBrowserOnRootPage())
      actor.attemptsTo(Open.browserOn(DebugGameTable()))
      return
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(Post.to("/gameTables").with {
        it.param("id", tableName)
      })
      actor.should(seeThatResponse { it.statusCode(SC_OK) })
      return
    }

    if (actor.abilityTo(AccessLocalIntegratedFrontend::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(OpenBrowserOnRootPage())
      actor.attemptsTo(Enter.theValue(tableName).into(Root.GAME_TABLE_NAME_INPUT).thenHit(Keys.ENTER))
      return
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
