package com.headissue.botc.e2e.actor

import com.github.javafaker.Faker
import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalIntegratedFrontend
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import com.headissue.botc.e2e.actor.Stage.*
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actors.Cast
import net.serenitybdd.screenplay.actors.OnlineCast
import net.serenitybdd.screenplay.rest.abilities.CallAnApi
import net.thucydides.core.ThucydidesSystemProperty

class Actors(val storyTeller: Actor, val players: GroupOfActors) {

  companion object {

    private val faker = Faker()

    fun forStage(stage: Stage): Actors? {

      // fixes side effect where all stages used the same tableName

      return when (stage) {
        LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS -> {
          val onlineCast = OnlineCast()
          val storyTeller = onlineCast.actorNamed("storyteller");
          val driver = storyTeller.abilityTo(BrowseTheWeb::class.java).driver
          /*
           mocked remote actions are performed by the storyteller itself through the frontend, that's why players use the same
           driver
         */
          val mockedRemoteActorsCast = Cast.whereEveryoneCan(BrowseTheWeb.with(driver))
          val players = players(mockedRemoteActorsCast)
          storyTeller.can(AccessLocalFrontendMockGameTable())
          players.can(AccessLocalFrontendMockGameTable())
          Actors(storyTeller, players)
        }

        LOCAL_REST_API -> {
          val tableName = faker.name().firstName()
          val cast = Cast.whereEveryoneCan(CallAnApi.at("http://localhost:8080/api"), AccessLocalRestAPI())
          val storyTeller = cast.actorNamed("storyteller")
          val players = players(cast)
          cast.actors.forEach { it.remember(Memories.TABLE_NAME, tableName) }
          Actors(storyTeller, players)
        }
        LOCAL_FRONTEND_INTEGRATED -> {
          val tableName = faker.name().firstName()
          System.setProperty(ThucydidesSystemProperty.WEBDRIVER_BASE_URL.preferredName(), "http://localhost:3000")
          val cast = OnlineCast()
          val storyTeller = cast.actorNamed("storyteller")
          val players = players(cast)
          cast.actors.forEach {
            it.remember(Memories.TABLE_NAME, tableName)
            it.can(AccessLocalIntegratedFrontend())
          }
          Actors(storyTeller, players)
        }
      }
    }

    private fun players(cast: Cast): GroupOfActors {
      val players = GroupOfActors()
      val actorNames = generateSequence { cast.actorNamed(faker.gameOfThrones().character()) }
          .take(5)
      actorNames.forEach { players.add(it) }
      return players
    }

  }
}