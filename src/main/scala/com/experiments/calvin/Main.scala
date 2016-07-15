package com.experiments.calvin

import java.nio.ByteBuffer

import boopickle.Default._
import com.experiments.calvin.models.{ItemV1, ShoppingCartV1}

object Main extends App {
  val shoppingCart = ShoppingCartV1(
    List(
      ItemV1(1, "chips"),
      ItemV1(2, "chocolate")
    )
  )

  val byteBuffer = Pickle.intoBytes(shoppingCart)
  // For Akka Persistence
  val byteArray = byteBuffer.array()
  val deserialized = Unpickle[ShoppingCartV1].fromBytes(ByteBuffer.wrap(byteArray))

  println(deserialized)
}
