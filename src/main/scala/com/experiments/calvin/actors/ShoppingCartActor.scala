package com.experiments.calvin.actors

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import com.experiments.calvin.actors.ShoppingCartActor.GetResult
import com.experiments.calvin.models.{ShoppingCartV1, ShoppingCartV2}

class ShoppingCartActor extends PersistentActor with ActorLogging {
  var state = ShoppingCartV2(List.empty)

  val updateState: ShoppingCartV2 => Unit = {
    sc: ShoppingCartV2 => state = sc
  }

  override def persistenceId: String = "shopping-cart-persistent-actor"

  override def receiveRecover: Receive = {
    case sc: ShoppingCartV2 =>
      log.info("recovering Shopping Cart from journal: {}", sc)
      updateState(sc)
  }

  override def receiveCommand: Receive = {
    case sc: ShoppingCartV2 => persist(sc)(updateState)
    case GetResult => sender() ! state
  }
}

object ShoppingCartActor {
  case object GetResult
}