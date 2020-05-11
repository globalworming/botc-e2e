package com.headissue.botc_unofficial.e2e.page

import net.serenitybdd.screenplay.targets.Target.the


class JoinGame {
  companion object {
    val ENTER_NAME = the("enter name input").locatedBy(".enterName input")
  }
}