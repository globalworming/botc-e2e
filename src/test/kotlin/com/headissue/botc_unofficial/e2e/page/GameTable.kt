package com.headissue.botc_unofficial.e2e.page

import net.serenitybdd.screenplay.targets.Target

object GameTable {
  val townsquare: TownSquare = TownSquare
  val grimoire: Grimoire = Grimoire
}

object TownSquare {
  val player: Target = Target.the("displayed players").locatedBy(".townSquare .player")
}

object Grimoire {
  val player: Target = Target.the("displayed players").locatedBy(".grimoire .player")
}
