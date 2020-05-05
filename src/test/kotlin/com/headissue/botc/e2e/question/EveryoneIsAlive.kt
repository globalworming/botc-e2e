package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.AccessLocalFrontendMockGameTable
import com.headissue.botc.e2e.ability.AccessLocalRestAPI
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.EventualConsequence.eventually
import net.serenitybdd.screenplay.GivenWhenThen.seeThat
import net.serenitybdd.screenplay.NoMatchingAbilityException
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible
import net.serenitybdd.screenplay.questions.WebElementQuestion

class EveryoneIsAlive : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    if (actor.abilityTo(AccessLocalFrontendMockGameTable::class.java) != null) {
      actor.should(eventually(seeThat(WebElementQuestion.the(".townSquare .dead"), isNotVisible())))
    }

    if (actor.abilityTo(AccessLocalRestAPI::class.java) != null) {
      return actor.asksFor(PlayersAtTable()).filter { it.dead == true }.isEmpty()
    }

    throw NoMatchingAbilityException(actor.name)
  }

}
