package com.headissue.botc_unofficial.e2e.question

import com.headissue.botc_unofficial.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.abilities.BrowseTheWeb
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*

class TheyNominated(val name: String) : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(BrowseTheWeb::class.java) != null) {
      val player = actor.asksFor(ThePlayer(name))
      assertThat(player.nominated, `is`(true))
      assertThat(player.nominatedBy, `is`(actor.name))
      return true
    }


    if (actor.abilityTo(AccessLocalRestAPI::
        class.java) != null) {
      return true
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
