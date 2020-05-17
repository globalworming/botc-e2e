package com.headissue.botc_unofficial.e2e.page

import net.serenitybdd.screenplay.targets.Target

object TownSquare {
  val player: Target = Target.the("displayed players").locatedBy(".townSquare .player")
}

object Grimoire {
  val player: Target = Target.the("displayed players").locatedBy(".grimoire .player")
  val nominatedPlayer: Target = Target.the("current nominated player").locatedBy(".nominations .nominated")
  val nominatedBy = Target.the("current player nominated by").locatedBy(".nominations .nominatedBy")
}
