package com.headissue.botc.e2e.question

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.targets.Target

class ItIsNight : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    return Target.the("night icon").locatedBy(".night").resolveFor(actor).isVisible
  }

}
