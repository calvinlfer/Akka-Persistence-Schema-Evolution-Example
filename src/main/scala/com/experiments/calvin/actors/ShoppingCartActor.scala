package com.experiments.calvin.actors

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import com.experiments.calvin.actors.ShoppingCartActor.GetResult
import com.experiments.calvin.models.ShoppingCartV1

class ShoppingCartActor extends PersistentActor with ActorLogging {
  var state = ShoppingCartV1(List.empty)

  val updateState: ShoppingCartV1 => Unit = {
    sc: ShoppingCartV1 => state = sc
  }

  override def persistenceId: String = "shopping-cart-persistent-actor"

  override def receiveRecover: Receive = {
    case sc: ShoppingCartV1 => {
      log.info("recovering Shopping Cart from journal: {}", sc)
      updateState(sc)
    }
  }

  override def receiveCommand: Receive = {
    case sc: ShoppingCartV1 => persist(sc)(updateState)
    case GetResult => sender() ! state
  }
}

object ShoppingCartActor {
  case object GetResult
}