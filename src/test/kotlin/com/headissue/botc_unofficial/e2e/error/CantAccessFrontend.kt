package com.headissue.botc_unofficial.e2e.error

class CantAccessFrontend : CompromisedTest {

  constructor(detailMessage: Any) : super(detailMessage)

  constructor(message: String, cause: Throwable) : super(message, cause)

}
