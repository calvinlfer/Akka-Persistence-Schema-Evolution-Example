package com.experiments.calvin


import akka.actor.{ActorSystem, Props}
import com.experiments.calvin.actors.ShoppingCartActor
import com.experiments.calvin.actors.ShoppingCartActor.GetResult
import com.experiments.calvin.models.{ItemV1, ShoppingCartV1}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {
  val shoppingCart = ShoppingCartV1(
    List(
      ItemV1(1, "chips"),
      ItemV1(2, "chocolate")
    )
  )

  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem(name = "example")
  val shoppingCartActor = actorSystem.actorOf(Props[ShoppingCartActor], name = "shopping-cart-actor")
  shoppingCartActor ! shoppingCart
  val futureResult = (shoppingCartActor ? GetResult).mapTo[ShoppingCartV1]
  val obtainedCart = Await.result(futureResult, 10 seconds)
  println(obtainedCart)
  actorSystem.terminate()
}
