package com.headissue.botc.e2e.test.happy

import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import com.headissue.botc.e2e.action.*
import com.headissue.botc.e2e.actor.Actors
import com.headissue.botc.e2e.actor.GroupOfActors
import com.headissue.botc.e2e.actor.Stage
import com.headissue.botc.e2e.actor.Stage.*
import com.headissue.botc.e2e.question.*
import net.serenitybdd.core.Serenity
import net.serenitybdd.junit.runners.SerenityParameterizedRunner
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.questions.CountQuestion
import net.thucydides.core.annotations.Pending
import net.thucydides.core.util.EnvironmentVariables
import net.thucydides.junit.annotations.TestData
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder
import org.hamcrest.core.IsCollectionContaining
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.NAME_ASCENDING


@RunWith(SerenityRunner::class)
@FixMethodOrder(NAME_ASCENDING)
class BotcHappyPathIT {

  lateinit var storyTeller: Actor
  lateinit var players: GroupOfActors
  lateinit var wePlayOn: Stage
  lateinit var environmentVariables: EnvironmentVariables

  @ExperimentalStdlibApi
  @Before
  fun setUp() {
    val stage = environmentVariables.getProperty("e2e.on.stage")
    assertThat("property \"e2e.on.stage\" must be defined and contain a valid value",
        Stage.values().map { it.name }.toList(), IsCollectionContaining.hasItem(stage))
    wePlayOn = valueOf(stage)
    val actors = Actors.forStage(wePlayOn) ?: throw RuntimeException("there are not actors configured for this stage")
    storyTeller = actors.storyTeller
    players = actors.players
    storyTeller.can(SeeGrimoire())
    players.can(SeeTownSquare())
  }

  @Test
  fun `1 when storyteller opens a new table, table is without players`() {
    storyTeller.attemptsTo(SetUpNewGameTable())
    storyTeller.attemptsTo(EnsureEmptyTableIsPresent())
  }

  @Test
  fun `2 when players join a table, the storyteller sees players have joined`() {
    `1 when storyteller opens a new table, table is without players`()
    players.forEach {
      it.attemptsTo(JoinGame())
    }
    storyTeller.should(eventually(seeThat(CountQuestion(PlayersAtTable()), `is`(5))))
  }


  @Test
  fun `3 when storyteller starts first night, players can see the town square`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    players[3].attemptsTo(EnsureInitialTownSquareIsDisplayed())
  }

  // FIXME, flakyness because of quick call to next turn, introduce global "i am fuzzy" marker to wait for it to be gone
  @Test
  fun `3 as storyteller progresses the story, players can see the updated town square`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
    players[3].should(eventually(seeThat(ItIsDay(), `is`(true))))

    storyTeller.should(eventually(seeThat(PlayerIsDead(players[1].name), `is`(false))))
    storyTeller.attemptsTo(KillPlayer(players[1].name))
    storyTeller.should(eventually(seeThat(PlayerIsDead(players[1].name), `is`(true))))
    storyTeller.should(eventually(seeThat(PlayerCanVote(players[1].name), `is`(true))))

    storyTeller.attemptsTo(KillPlayer(players[2].name))
    storyTeller.attemptsTo(MarkPlayerUsedVote(players[2].name))
    players[3].should(eventually(seeThat(PlayerIsDead(players[2].name), `is`(true))))
    players[3].should(eventually(seeThat(PlayerCanVote(players[2].name), `is`(false))))
  }


  @Test
  fun `3 when storyteller starts first night, characters are randomly assigned`() {
    Assume.assumeThat(wePlayOn, anyOf(`is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS)))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.should(eventually(seeThat(ItIsNight(), `is`(true))))
    storyTeller.should(seeThat(CharactersInPlay(), IsIterableContainingInOrder.contains(
        "Slayer", "Librarian", "Spy", "Imp", "Empath"
    )))
  }

  @Test
  fun `3 when storyteller starts first day, it is daytime`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
  }

  @Pending
  @Test
  fun `3 when storyteller starts next phase, abilities are marked as available agin`() {
    Assume.assumeThat(wePlayOn, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS),
        `is`(LOCAL_FRONTEND_INTEGRATED)
    ))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
  }

  @Pending
  @Test
  fun `4 celebrateEvilWinning`() {
    Assume.assumeThat(wePlayOn, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS),
        `is`(LOCAL_FRONTEND_INTEGRATED)
    ))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
  }

  @Pending
  @Test
  fun `4 good triumphing`() {
    Assume.assumeThat(wePlayOn, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS),
        `is`(LOCAL_FRONTEND_INTEGRATED)
    ))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
  }


  @After
  fun tearDown() {
    Serenity.getWebdriverManager().closeCurrentDrivers()
  }
}
