package com.headissue.botc.e2e

import com.github.javafaker.Faker
import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccesLocalRestAPI
import com.headissue.botc.e2e.ability.SeeGrimoire
import com.headissue.botc.e2e.ability.SeeTownSquare
import com.headissue.botc.e2e.action.*
import com.headissue.botc.e2e.actor.Memories
import com.headissue.botc.e2e.question.*
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.screenplay.Ability
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.actors.Cast
import net.serenitybdd.screenplay.actors.OnlineCast
import net.serenitybdd.screenplay.questions.CountQuestion
import net.serenitybdd.screenplay.rest.abilities.CallAnApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.collection.IsIterableContainingInOrder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(SerenityRunner::class)
class BotcHappyPathIT {

  // FIXME get from env
  //var onWhatStageShouldWePlay = MyStage.LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS
  var onWhatStageShouldWePlay = MyStage.LOCAL_REST_API

  lateinit var storyTeller: Actor
  lateinit var players: GroupOfActors

  @Before
  fun setUp() {
    val actors = MyActors.forStage(onWhatStageShouldWePlay)
        ?: throw RuntimeException("there are not actors configured for this stage")
    storyTeller = actors.storyTeller
    players = actors.players
    storyTeller.can(SeeGrimoire())
    players.can(SeeTownSquare())
  }

  @Test
  fun `when storyteller opens a new table, table is without players`() {
    storyTeller.attemptsTo(SetUpNewGameTable())
    storyTeller.attemptsTo(EnsureEmptyTableIsPresent())
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

class MyActors(val storyTeller: Actor, val players: GroupOfActors) {

  companion object {

    private val faker = Faker()

    fun forStage(stage: MyStage): MyActors? {

      return when (stage) {

        MyStage.LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS -> {
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
          MyActors(storyTeller, players)
        }

        MyStage.LOCAL_REST_API -> {
          val cast = Cast.whereEveryoneCan(CallAnApi.at("http://localhost:8080"), AccesLocalRestAPI())
          val storyTeller = cast.actorNamed("storyteller")
          val players = GroupOfActors()
          generateSequence { cast.actorNamed(faker.gameOfThrones().character()) }
              .take(5).forEach { players.add(it) }
          cast.actors.forEach { it.remember(Memories.TABLE_NAME, faker.rickAndMorty().character()) }
          MyActors(storyTeller, players)
        }
      }
    }

  }
}

enum class MyStage { LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS, LOCAL_REST_API }

class GroupOfActors : AbstractMutableList<Actor>() {

  private var actors: MutableList<Actor> = mutableListOf()

  fun <T : Ability> can(doSomething: T) {
    forEach { it.can(doSomething) }
  }

  override val size: Int
    get() = actors.size

  override fun add(element: Actor): Boolean = actors.add(element)

  override fun iterator(): MutableIterator<Actor> = actors.iterator()

  override fun get(index: Int): Actor = actors[index]

  override fun add(index: Int, element: Actor) {
    TODO("Not yet implemented")
  }

  override fun removeAt(index: Int): Actor {
    TODO("Not yet implemented")
  }

  override fun set(index: Int, element: Actor): Actor {
    TODO("Not yet implemented")
  }
}
