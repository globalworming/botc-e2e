package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccesLocalRestAPI
import com.headissue.botc.e2e.actor.Memories
import com.headissue.botc.e2e.error.CantAccessFrontend
import com.headissue.botc.e2e.page.DebugGameTable
import com.headissue.botc.e2e.page.Root
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.actions.Open
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible
import net.serenitybdd.screenplay.questions.WebElementQuestion.the
import net.serenitybdd.screenplay.rest.interactions.Post

open class SetUpNewGameTable : Performable {
  override fun <T : Actor> performAs(actor: T) {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      actor.attemptsTo(Open.browserOn(Root()))
      actor.should(eventually(seeThat(
          the(".App"), isVisible()))
          .orComplainWith(CantAccessFrontend::class.java, "local fe running?"))
      actor.attemptsTo(Open.browserOn(DebugGameTable()))
      return
    }

    if (actor.abilityTo(AccesLocalRestAPI::class.java) != null) {
      val tableName: String = actor.recall(Memories.TABLE_NAME)
      actor.attemptsTo(Post.to("/gametables").with {
        it.param("name", tableName)
      })
      return
    }

    throw NoMatchingAbilityException(this::class.simpleName)
  }

}
