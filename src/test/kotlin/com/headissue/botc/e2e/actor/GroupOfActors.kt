package com.headissue.botc.e2e.actor

import net.serenitybdd.screenplay.Ability
import net.serenitybdd.screenplay.Actor

class GroupOfActors : AbstractMutableList<Actor>() {

  private var actors: MutableList<Actor> = mutableListOf()

  fun <T : Ability> can(doSomething: T) {
    forEach { it.can(doSomething) }
  }

  override val size: Int
    get() = actors.size

  override fun add(element: Actor): Boolean = actors.add(element)

  override fun iterator(): MutableIterator<Actor> = actors.iterator()

  override fun get(index: Int): Actor = actors[index]

  override fun add(index: Int, element: Actor) {
    TODO("Not yet implemented")
  }

  override fun removeAt(index: Int): Actor {
    TODO("Not yet implemented")
  }

  override fun set(index: Int, element: Actor): Actor {
    TODO("Not yet implemented")
  }
}