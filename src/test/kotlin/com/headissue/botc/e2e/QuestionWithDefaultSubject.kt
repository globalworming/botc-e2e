package com.headissue.botc.e2e

import net.serenitybdd.screenplay.Question

abstract class QuestionWithDefaultSubject<T> : Question<T> {

  override fun getSubject(): String {
    return formattedClassName()
  }

  private fun formattedClassName() = this.javaClass.simpleName.replace("([a-z])([A-Z])".toRegex(), "$1 $2")

  override fun toString(): String {
    return formattedClassName()
  }
}
