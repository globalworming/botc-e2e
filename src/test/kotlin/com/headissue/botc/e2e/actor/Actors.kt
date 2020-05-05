package com.headissue.botc.e2e.actor

import com.github.javafaker.Faker
import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actors.Cast
import net.serenitybdd.screenplay.actors.OnlineCast
import net.serenitybdd.screenplay.rest.abilities.CallAnApi

class Actors(val storyTeller: Actor, val players: GroupOfActors) {

  companion object {

    private val faker = Faker()

    fun forStage(stage: Stages): Actors? {

      return when (stage) {

        Stages.LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS -> {
          val onlineCast = OnlineCast()
          val storyTeller = onlineCast.actorNamed("storyteller");
          val driver = storyTeller.abilityTo(BrowseTheWeb::class.java).driver
          /*
           mocked remote actions are performed by the storyteller itself through the frontend, that's why players use the same
           driver
         */
          val mockedRemoteActorsCast = Cast.whereEveryoneCan(BrowseTheWeb.with(driver))
          val players = GroupOfActors()
          generateSequence { mockedRemoteActorsCast.actorNamed(faker.gameOfThrones().character()) }
              .take(5).forEach { players.add(it) }
          storyTeller.can(AccessLocalFrontendMockGameTable())
          players.can(AccessLocalFrontendMockGameTable())
          Actors(storyTeller, players)
        }

        Stages.LOCAL_REST_API -> {
          val cast = Cast.whereEveryoneCan(CallAnApi.at("http://localhost:8080"), AccessLocalRestAPI())
          val storyTeller = cast.actorNamed("storyteller")
          val players = GroupOfActors()
          generateSequence { cast.actorNamed(faker.gameOfThrones().character()) }
              .take(5).forEach { players.add(it) }
          val tableName = faker.name().firstName()
          cast.actors.forEach { it.remember(Memories.TABLE_NAME, tableName) }
          Actors(storyTeller, players)
        }
      }
    }

  }
}