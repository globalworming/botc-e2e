package com.headissue.botc.e2e.page

import net.serenitybdd.core.pages.PageObject
import net.serenitybdd.screenplay.targets.Target
import net.serenitybdd.screenplay.targets.Target.the
import net.thucydides.core.annotations.DefaultUrl

@DefaultUrl("http://localhost:3000")
class Root : PageObject() {
  companion object {
    val GAME_TABLE_NAME_INPUT: Target = the("game table name input").locatedBy(".setupGameTable input")
  }

}
