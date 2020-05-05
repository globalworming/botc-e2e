package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.action.GetTableInfo
import com.headissue.botc.e2e.model.Player
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.rest.questions.LastResponse
import net.serenitybdd.screenplay.targets.Target

class PlayersAtTable : QuestionWithDefaultSubject<List<Player>>() {
  override fun answeredBy(actor: Actor): List<Player> {

    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      val playerElements = Target.the("displayed players").locatedBy(".grimoire .player").resolveAllFor(actor)
      return playerElements.map {
        val name = it.findBy<WebElementFacade>(".name").text
        Player(name = name)
      }
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      actor.attemptsTo(GetTableInfo())
      return actor.asksFor(LastResponse()).jsonPath().getList("players", Player::class.java)
    }

    throw NoMatchingAbilityException(actor.name)
  }


}
