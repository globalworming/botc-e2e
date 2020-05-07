package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import com.headissue.botc.e2e.action.GetTableInfo
import com.headissue.botc.e2e.model.Player
import com.headissue.botc.e2e.page.GameTable
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.rest.questions.LastResponse

class PlayersAtTable : QuestionWithDefaultSubject<List<Player>>() {
  override fun answeredBy(actor: Actor): List<Player> {

    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      if (actor.abilityTo(SeeGrimoire::class.java) != null) {
        val playerElements = GameTable.grimoire.player.resolveAllFor(actor)
        return playerElements.map {
          val name = it.findBy<WebElementFacade>(".name").text
          val dead = it.findBy<WebElementFacade>(".dead input").isSelected
          val canVote = !dead || it.findBy<WebElementFacade>(".canVote input").isSelected
          Player(name = name, dead = dead, canVote = canVote)
        }
      }
      if (actor.abilityTo(SeeTownSquare::class.java) != null) {
        val playerElements = GameTable.townsquare.player.resolveAllFor(actor)
        return playerElements.map {
          val name = it.findBy<WebElementFacade>(".name").text
          val dead = it.thenFindAll(".dead").size > 0
          val canVote = !dead || it.thenFindAll(".canVote").size > 0
          Player(name = name, dead = dead, canVote = canVote)

        }
      }
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      actor.attemptsTo(GetTableInfo())
      return actor.asksFor(LastResponse()).jsonPath().getList("players", Player::class.java)
    }

    throw NoMatchingAbilityException(actor.name)
  }


}
