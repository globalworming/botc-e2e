package com.headissue.botc.e2e

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.targets.Target

class ItIsDay : QuestionWithDefaultSubject<Boolean>() {
  override fun answeredBy(actor: Actor): Boolean {
    return Target.the("day icon").locatedBy(".day").resolveFor(actor).isVisible
  }

}
