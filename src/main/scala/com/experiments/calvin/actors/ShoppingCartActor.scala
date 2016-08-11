package com.experiments.calvin.actors

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import com.experiments.calvin.actors.ShoppingCartActor.GetResult
import com.experiments.calvin.models.ShoppingCartV3

class ShoppingCartActor extends PersistentActor with ActorLogging {
  var state = ShoppingCartV3(List.empty)

  val updateState: ShoppingCartV3 => Unit = {
    sc: ShoppingCartV3 => state = sc
  }

  override def persistenceId: String = "shopping-cart-persistent-actor"

  override def receiveRecover: Receive = {
    case sc: ShoppingCartV3 =>
      log.info("recovering Shopping Cart from journal: {}", sc)
      updateState(sc)
  }

  override def receiveCommand: Receive = {
    case sc: ShoppingCartV3 => persist(sc)(updateState)
    case GetResult => sender() ! state
  }
}

object ShoppingCartActor {
  case object GetResult
}