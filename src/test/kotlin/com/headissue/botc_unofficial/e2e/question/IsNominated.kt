package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import com.headissue.botc_unofficial.e2e.ability.SeeGrimoire
import com.headissue.botc_unofficial.e2e.ability.SeeTownSquare
import com.headissue.botc_unofficial.e2e.page.Grimoire
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence
import net.serenitybdd.screenplay.GivenWhenThen
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import net.serenitybdd.screenplay.questions.targets.TheTarget.*
import org.hamcrest.CoreMatchers.*

class IsNominated(val name: String, val byName: String? = null) : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      if (actor.abilityTo(SeeTownSquare::class.java) != null) {
        return ThePlayer(name).answeredBy(actor).nominated
      }
      if (actor.abilityTo(SeeGrimoire::class.java) != null) {
        actor.should(EventualConsequence.eventually(GivenWhenThen.seeThat(
            valueOf(Grimoire.nominatedPlayer), `is`(name)
        )))
        actor.should(EventualConsequence.eventually(GivenWhenThen.seeThat(
            valueOf(Grimoire.nominatedBy), `is`(byName)
        )))
        return true

      }

      if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
        return true
      }

    }
    throw NoMatchingAbilityException(actor.name)
  }

  fun by(byName: String): IsNominated {
    return IsNominated(name, byName)
  }
}
