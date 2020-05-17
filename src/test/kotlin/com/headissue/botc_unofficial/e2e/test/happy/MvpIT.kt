package com.headissue.botc_unofficial.e2e.test.happy

import com.headissue.botc_unofficial.e2e.ability.SeeGrimoire
import com.headissue.botc_unofficial.e2e.ability.SeeTownSquare
import com.headissue.botc_unofficial.e2e.action.CallForNominations
import com.headissue.botc_unofficial.e2e.action.DeclareEvilWins
import com.headissue.botc_unofficial.e2e.action.DeclareGoodWins
import com.headissue.botc_unofficial.e2e.action.EnsureCharactersWereAssigned
import com.headissue.botc_unofficial.e2e.action.EnsureEmptyTableIsPresent
import com.headissue.botc_unofficial.e2e.action.EnsureInitialTownSquareIsDisplayed
import com.headissue.botc_unofficial.e2e.action.JoinGame
import com.headissue.botc_unofficial.e2e.action.KillPlayer
import com.headissue.botc_unofficial.e2e.action.MarkPlayerUsedVote
import com.headissue.botc_unofficial.e2e.action.NominatePlayer
import com.headissue.botc_unofficial.e2e.action.SetUpNewGameTable
import com.headissue.botc_unofficial.e2e.action.StartGame
import com.headissue.botc_unofficial.e2e.action.StartNextDay
import com.headissue.botc_unofficial.e2e.actor.Actors
import com.headissue.botc_unofficial.e2e.actor.GroupOfActors
import com.headissue.botc_unofficial.e2e.actor.Stage
import com.headissue.botc_unofficial.e2e.actor.Stage.*
import com.headissue.botc_unofficial.e2e.question.EvilWon
import com.headissue.botc_unofficial.e2e.question.GoodWon
import com.headissue.botc_unofficial.e2e.question.IsNominated
import com.headissue.botc_unofficial.e2e.question.ItIsDay
import com.headissue.botc_unofficial.e2e.question.ItIsNight
import com.headissue.botc_unofficial.e2e.question.NumberOfPlayersAtTable
import com.headissue.botc_unofficial.e2e.question.PlayerCanVote
import com.headissue.botc_unofficial.e2e.question.PlayerIsDead
import com.headissue.botc_unofficial.e2e.question.TheyNominated
import net.serenitybdd.core.Serenity
import net.serenitybdd.junit.runners.SerenityParameterizedRunner
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.*
import net.serenitybdd.screenplay.GivenWhenThen.*
import net.thucydides.core.annotations.Issues
import net.thucydides.core.annotations.Narrative
import net.thucydides.core.annotations.Pending
import net.thucydides.core.util.EnvironmentVariables
import net.thucydides.junit.annotations.TestData
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assume
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.NAME_ASCENDING

@Narrative(
    title = "BOTC unofficial mvp happy path e2e",
    text = ["""
  The minimal valuable product allows multiple users to connect to a game of botc. One client being the storyteller 
  having a privileged view on the game, called Grimoire. They are driving the game and handle the mechanics. 
  
  The players have very little information and view the games TownSquare and interact with each other to reach a 
  win/fail state.
"""])
@FixMethodOrder(NAME_ASCENDING)
@RunWith(SerenityParameterizedRunner::class)
/**
 * test are numbered, so basic test run first. with junit @Rule or surefire skipAfterFailureCount you could
 * prevent execution of complex tests if the basics fail
 */
class MvpIT(private val wePlayOn: Stage) {

  companion object {
    @JvmStatic
    @TestData
    fun data(): Collection<Array<Stage>> = values().map { arrayOf(it) }
  }

  private lateinit var storyTeller: Actor
  private lateinit var players: GroupOfActors

  @Before
  fun setUp() {
    val actors = Actors.forStage(wePlayOn)
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
    storyTeller.should(eventually(seeThat(NumberOfPlayersAtTable(), `is`(5))))
  }


  @Test
  fun `3 when storyteller starts first night, players can see the town square`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    players[3].attemptsTo(EnsureInitialTownSquareIsDisplayed())
  }

  @Test
  // FIXME flakey on frontend, see issue
  @Issues("#13")
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
    Assume.assumeThat(wePlayOn, anyOf(`is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS), `is`(LOCAL_FRONTEND_INTEGRATED)))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.should(eventually(seeThat(ItIsNight(), `is`(true))))
    storyTeller.attemptsTo(EnsureCharactersWereAssigned())
  }

  @Test
  fun `3 when storyteller starts first day, it is daytime`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.attemptsTo(StartNextDay())
    storyTeller.should(eventually(seeThat(ItIsDay(), `is`(true))))
  }

  @Test
  // fixme not part of mvp
  fun `4 when storyteller calls for nominations, players can nominate`() {
    Assume.assumeThat(wePlayOn, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS)
    ))
    `3 when storyteller starts first day, it is daytime`()
    storyTeller.attemptsTo(CallForNominations())
    players[1].attemptsTo(NominatePlayer(players[2].name))
    players[1].should(eventually(seeThat(TheyNominated(players[2].name), `is`(true))))
    storyTeller.should(eventually(seeThat(IsNominated(players[2].name).by(players[1].name), `is`(true))))

    // later when voted concluded
    //storyTeller.should(eventually(seeThat(PlayerUsedNomination(players[1].name), `is`(true))))
    //players[1].should(eventually(seeThat(PlayerUsedNomination(players[1].name), `is`(true))))

  }

  @Pending
  @Test
  fun `3 when storyteller starts next phase, abilities are marked as available again`() {
    Assume.assumeThat(wePlayOn, anyOf(
        `is`(LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS),
        `is`(LOCAL_FRONTEND_INTEGRATED)
    ))
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
  }

  @Test
  fun `3 celebrate evil wins`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.attemptsTo(DeclareEvilWins())
    storyTeller.should(seeThat(EvilWon(), `is`(true)))
  }

  @Test
  fun `3 celebrate good triumphs`() {
    `2 when players join a table, the storyteller sees players have joined`()
    storyTeller.attemptsTo(StartGame())
    storyTeller.attemptsTo(DeclareGoodWins())
    storyTeller.should(seeThat(GoodWon(), `is`(true)))
  }

  @After
  fun tearDown() {
    Serenity.getWebdriverManager().closeCurrentDrivers()
  }
}