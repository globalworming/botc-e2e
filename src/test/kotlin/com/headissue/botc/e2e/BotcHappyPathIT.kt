package com.headissue.botc.e2e

import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import com.headissue.botc.e2e.action.*
import com.headissue.botc.e2e.actor.Actors
import com.headissue.botc.e2e.actor.GroupOfActors
import com.headissue.botc.e2e.actor.Stages.*
import com.headissue.botc.e2e.question.*
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.questions.CountQuestion
import net.thucydides.core.annotations.Pending
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.collection.IsIterableContainingInOrder
import org.junit.Assume
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.*


@RunWith(SerenityRunner::class)
@FixMethodOrder(NAME_ASCENDING)
class BotcHappyPathIT {

  // FIXME get from env
  var onWhatStageShouldWePlay =
      //LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS
      //LOCAL_REST_API
      LOCAL_FRONTEND_INTEGRATED

  lateinit var storyTeller: Actor
  lateinit var players: GroupOfActors

  @Before
  fun setUp() {
    val actors = Actors.forStage(onWhatStageShouldWePlay)
        ?: throw RuntimeException("there are not actors configured for this stage")
    storyTeller = actors.storyTeller
    players = actors.players
    storyTeller.can(SeeGrimoire())
    players.can(SeeTownSquare())
  }

  @Test
  fun `#1 when storyteller opens a new table, table is without players`() {
    storyTeller.attemptsTo(SetUpNewGameTable())
    storyTeller.attemptsTo(EnsureEmptyTableIsPresent())
  }

  @Test
  fun `#2 when players join a table, the storyteller sees players have joined`() {
    `#1 when storyteller opens a new table, table is without players`()
    players.forEach {
      it.attemptsTo(JoinGame())
    }
    storyTeller.should(seeThat(CountQuestion(PlayersAtTable()), `is`(5)))
  }


  @Test
  fun `#3 when storyteller starts first night, players can see the town square`() {
    `#2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    players[3].attemptsTo(EnsureInitialTownSquareIsDisplayed())
  }

  @Test
  fun `#3 as storyteller progresses the story, players can see the updated town square`() {
    `#2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
    players[3].should(eventually(seeThat(ItIsDay(), `is`(true))))

    storyTeller.should(eventually(seeThat(PlayerIsDead(players[1].name), `is`(false))))
    storyTeller.attemptsTo(KillPlayer(players[1].name))
    storyTeller.should(eventually(seeThat(PlayerIsDead(players[1].name), `is`(true))))
    storyTeller.should(eventually(seeThat(PlayerCanVote(players[1].name), `is`(true))))

    storyTeller.attemptsTo(KillPlayer(players[2].name))
    storyTeller.attemptsTo(MarkPlayerUsedVote(players[2].name))
    players[1].should(eventually(seeThat(PlayerIsDead(players[2].name), `is`(true))))
    players[1].should(eventually(seeThat(PlayerCanVote(players[2].name), `is`(false))))
  }


  @Test
  fun `#3 when storyteller starts first night, characters are randomly assigned`() {
    Assume.assumeThat(onWhatStageShouldWePlay, anyOf(`is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS)))
    `#2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.should(eventually(seeThat(ItIsNight(), `is`(true))))
    storyTeller.should(seeThat(CharactersInPlay(), IsIterableContainingInOrder.contains(
        "Slayer", "Librarian", "Spy", "Imp", "Empath"
    )))
  }

  @Test
  fun `#3 when storyteller starts first day, it is daytime`() {
    `#2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
  }

  @Pending
  @Test
  fun `#3 when storyteller starts next phase, abilities are marked as available agin`() {
    Assume.assumeThat(onWhatStageShouldWePlay, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS),
        `is`(LOCAL_FRONTEND_INTEGRATED)
    ))
    `#2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartFirstNight())

  }

}