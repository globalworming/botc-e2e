package com.headissue.botc.e2e.action

import com.headissue.botc.e2e.actor.Memories
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.rest.interactions.Get
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse
import org.apache.http.HttpStatus.SC_OK

open class GetTableInfo : Performable {
  override fun <T : Actor> performAs(actor: T) {
    val tableName = actor.recall(Memories.TABLE_NAME) as String
    actor.attemptsTo(Get.resource("/gametable/${tableName}"))
    actor.should(seeThatResponse { it.statusCode(SC_OK) })
  }

}
