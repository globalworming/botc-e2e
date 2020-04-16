package com.headissue.botc.e2e.question

import com.headissue.botc.e2e.ability.SeeGrimoire
import net.serenitybdd.core.pages.WebElementFacade
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.targets.Target

class Player(val name: String) : QuestionWithDefaultSubject<WebElementFacade>() {
  override fun answeredBy(actor: Actor): WebElementFacade {

    var boardOrGrimoire = if (actor.abilityTo(SeeGrimoire::class.java) != null) ".grimoire" else ".townSquare"
    return Target.the("player named " + name)
        .locatedBy(boardOrGrimoire + " .player")
        .resolveAllFor(actor).stream()
        .filter { it.textContent.contains(name) }.findFirst().get()
  }

}
