package com.experiments.calvin


import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.experiments.calvin.actors.ShoppingCartActor
import com.experiments.calvin.actors.ShoppingCartActor.GetResult
import com.experiments.calvin.models.{ItemV2, ShoppingCartV2}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {
  val shoppingCart = ShoppingCartV2(
    List(
      ItemV2(1, "lays chips", "cant have just one bite"),
      ItemV2(2, "caramilk chocolate", "calories")
    )
  )

  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem(name = "example")
  val shoppingCartActor = actorSystem.actorOf(Props[ShoppingCartActor], name = "shopping-cart-actor")
  shoppingCartActor ! shoppingCart
  val futureResult = (shoppingCartActor ? GetResult).mapTo[ShoppingCartV2]
  val obtainedCart = Await.result(futureResult, 10 seconds)
  println(obtainedCart)
  actorSystem.terminate()
}
