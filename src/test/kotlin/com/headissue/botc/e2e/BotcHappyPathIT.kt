package com.headissue.botc.e2e

import com.github.javafaker.Faker
import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import com.headissue.botc.e2e.action.*
import com.headissue.botc.e2e.question.*
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
  val players = generateSequence { mockedRemoteActorsCast.actorNamed(faker.gameOfThrones().character()) }
      .take(5).toList()

  @Before
  fun setUp() {
    storyTeller.can(SeeGrimoire())
    players.forEach { it.can(SeeTownSquare()) }
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
    players.forEach {
      it.attemptsTo(JoinGame())
    }
    storyTeller.should(seeThat(CountQuestion(PlayersAtTable()), `is`(5)))
  }


  @Test
  fun `when storyteller starts first night, players can see the town square`() {
    `when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    players[3].attemptsTo(EnsureInitialTownSquareIsDisplayed())
  }

  @Test
  fun `as storyteller progresses the story, players can see the updated town square`() {
    `when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
    players.forEach { it.should(eventually(seeThat(ItIsDay(), `is`(true)))) }

    storyTeller.attemptsTo(KillPlayer(players[1].name))
    players[2].should(eventually(seeThat(PlayerIsDead(players[1].name), `is`(true))))
    players[2].should(eventually(seeThat(PlayerHasNotUsedVote(players[1].name), `is`(true))))

    storyTeller.attemptsTo(KillPlayer(players[2].name))
    storyTeller.attemptsTo(MarkPlayerUsedVote(players[2].name))
    players[1].should(eventually(seeThat(PlayerIsDead(players[2].name), `is`(true))))
    players[1].should(eventually(seeThat(PlayerHasUsedVote(players[2].name), `is`(true))))
  }


  @Test
  fun `when storyteller starts first night, characters are randomly assigned`() {
    `when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.should(eventually(seeThat(ItIsNight(), `is`(true))))
    storyTeller.should(seeThat(CharactersInPlay(), IsIterableContainingInOrder.contains(
        "Slayer", "Librarian", "Spy", "Imp", "Empath"
    )))
  }

  @Test
  fun `when storyteller starts first day, it is daytime`() {
    `when storyteller starts first night, characters are randomly assigned`()
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
  }

}
