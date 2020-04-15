package com.headissue.botc.e2e

import com.github.javafaker.Faker
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actors.Cast
import net.serenitybdd.screenplay.actors.OnlineCast
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible
import net.serenitybdd.screenplay.questions.CountQuestion
import net.serenitybdd.screenplay.questions.WebElementQuestion.the
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.collection.IsIterableContainingInOrder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(SerenityRunner::class)
class BotcHappyPathIT {

  val onlineCast = OnlineCast()

  val storyTeller: Actor = onlineCast.actorNamed("storyTeller")
  /*
    mocked remote actions are performed by the storyteller itself through the frontend, that's why players use the same
    driver
  */
  val mockedRemoteActorsCast = Cast.whereEveryoneCan(BrowseTheWeb.with(storyTeller.abilityTo(BrowseTheWeb::class
      .java).driver))
  val faker = Faker()
  val players = generateSequence { mockedRemoteActorsCast.actorNamed(faker.gameOfThrones().character()) }.take(20).toList()

  @Before
  fun setUp() {
  }

  @Test
  fun `when storyteller opens a new table, table is without players`() {
    storyTeller.attemptsTo(SetUpNewGameTable())
    storyTeller.should(eventually(seeThat(the(".grimoire"), isVisible())))
    storyTeller.should(eventually(seeThat(the(".noPlayers"), isVisible())))
}

  @Test
  fun `when players join a table, the storyteller sees players have joined`() {
    `when storyteller opens a new table, table is without players`()
    // not very readable.. can we make it fivePlayers.attemptTo(JoinGame()) ?
    players.stream().limit(5).forEach {
      it.attemptsTo(JoinGame())
    }
    storyTeller.should(seeThat(CountQuestion(PlayersAtTable()), `is`(5)))
  }

  @Test
  fun `when storyteller starts first night, characters are randomly assigned`() {
    `when storyteller opens a new table, table is without players`()
    `when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.should(seeThat(CharactersInPlay(), IsIterableContainingInOrder.contains(
        "Slayer", "Librarian", "Spy", "Imp", "Empath"
    )))
  }

}
