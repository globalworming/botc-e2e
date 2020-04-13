package com.headissue.botc.e2e

import net.serenitybdd.core.exceptions.CausesCompromisedTestFailure

open class CompromisedTest : AssertionError, CausesCompromisedTestFailure {

  constructor(detailMessage: Any) : super(detailMessage)

  constructor(message: String, cause: Throwable) : super(message, cause)

}