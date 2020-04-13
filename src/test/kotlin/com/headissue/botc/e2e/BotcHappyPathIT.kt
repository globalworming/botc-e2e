package com.headissue.botc.e2e

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnlineCast
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(SerenityRunner::class)
class BotcHappyPathIT {

  val onlineCast = OnlineCast()

  val storyTeller: Actor = onlineCast.actorNamed("storyTeller")

  @Before
  fun setUp() {
    storyTeller.can(AccessRunningLocalDebugFrontend())
  }

  @Test
  fun `when storyteller opens a new table, table is without players`() {
    storyTeller.attemptsTo(SetUpNewGameTable())
  }

}
