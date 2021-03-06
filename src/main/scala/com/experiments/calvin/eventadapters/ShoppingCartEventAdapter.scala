package com.experiments.calvin.eventadapters

import akka.persistence.journal.{EventAdapter, EventSeq}
import com.experiments.calvin.models._

/**
  * This class upgrades old events from the event journal
  *
  * actor -> message -> event adapter -> serializer -> journal
  * actor <- message <- event adapter <- serializer <- journal
  */
class ShoppingCartEventAdapter extends EventAdapter {
  // Return the manifest (type hint) that will be provided in the `fromJournal` method.
  // serializer handles this, I only care about classes
  override def manifest(event: Any): String = ""

  // Pass through to serializer since I'm always persisting the latest events
  override def toJournal(event: Any): Any = {
    println(s"Event adapter is sending ${event.getClass.getName} to serializer as passthrough")
    event
  }

  override def fromJournal(event: Any, manifest: String): EventSeq = event match {
    case sc @ ShoppingCartV1(itemsV1) =>
      println("Reading V1 from journal and doing promotion to V3")
      EventSeq(
      // do promotion
      ShoppingCartV3(itemsV1.map(each => ItemV3(each.id, each.name, "")))
    )
    case sc @ ShoppingCartV2(itemsV2) =>
      println("Reading V2 from journal and doing promotion to V3")
      EventSeq(
      // do promotion
      ShoppingCartV3(itemsV2.map(each => ItemV3(each.id, each.name, each.description)))
    )
    case sc: ShoppingCartV3 =>
      println("V3 event encountered, no promotion needed")
      EventSeq(sc)
  }
}
