package com.experiments.calvin.serialization

import java.nio.ByteBuffer

import akka.serialization.SerializerWithStringManifest
import com.experiments.calvin.models.{ShoppingCartV1, ShoppingCartV2}
import boopickle.Default._

/**
  * Class is responsible for serializing ShoppingCarts before they make their way into the Journal/Snapshot journal
  * and when retrieving these items from the journals.
  */
class ShoppingCartSerializer extends SerializerWithStringManifest {
  val ShoppingCartV1Manifest = classOf[ShoppingCartV1].getName
  val ShoppingCartV2Manifest = classOf[ShoppingCartV2].getName

  // has to be unique
  // http://doc.akka.io/docs/akka/current/scala/serialization.html#SerializerwithStringManifest
  override def identifier: Int = 51109916

  // The manifest (type hint) that will be provided in the fromBinary method
  override def manifest(o: AnyRef): String = o match {
    case _: ShoppingCartV1 => ShoppingCartV1Manifest
    case _: ShoppingCartV2 => ShoppingCartV2Manifest
  }

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case v1: ShoppingCartV1 => Pickle.intoBytes(v1).array()
    case v2: ShoppingCartV2 => Pickle.intoBytes(v2).array()
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case ShoppingCartV1Manifest => Unpickle[ShoppingCartV1].fromBytes(ByteBuffer.wrap(bytes))
    case ShoppingCartV2Manifest => Unpickle[ShoppingCartV2].fromBytes(ByteBuffer.wrap(bytes))
  }
}
