# Akka Schema Evolution with Shopping Carts and Event Adapters #
This example is intended to demonstrate how to perform Schema Evolution 
without using an encoding mechanism that supports schema evolution 
(e.g. Protocol Buffers, Avro and Thrift).

In the following example, we use [BooPickle](https://github.com/ochrons/boopickle) as our encoding mechanism 
which only performs encoding and does not support schema evolution 
(we are treating the encoding mechanism like it doesn't support evolution). 
We rely on Event Adapters to promote from lower versions to higher versions 
which essentially does the Schema Evolution.

Here is what the set up looks like when persisting on the latest commit:
`Shopping Cart Actor -> Persist Message Vn -> Event Adapter (pass through) -> Serializer -> Journal`

**Note:** the event adapter is not being used for Vn events since its a pass-through

Here is the setup for recovering where Vn (n=3) is the latest version:

**V1**:

`Shopping Cart Actor <- Recover Message V3 <- Event Adapter (promote V1 ~> V3) <- Serializer (V1) <- Journal`

**V2**:

`Shopping Cart Actor <- Recover Message V3 <- Event Adapter (promote V2 ~> V3) <- Serializer (V2) <- Journal`

**Vn**:

`Shopping Cart Actor <- Recover Message Vn <- Event Adapter (pass through) <- Serializer (Vn) <- Journal`

**Note:** the event adapter is not being used for Vn events since its a pass-through

## How to use the application ##
- Revert to commit [f6058d3](https://github.com/calvinlfer/Akka-Persistence-Schema-Evolution-Example/commit/f6058d3ade3c5b1b5c6e0c73adcddb9c0db3eb1a) to persist V1 events
    - `git checkout f6058d3`
- Revert to commit [bf3d644](https://github.com/felipefzdz/Akka-Persistence-Schema-Evolution-Example/commit/bf3d64445eca98265ec735003b98401544c9b1df) to persist V2 events and see the promotion from V1 ~> V2
    - `git checkout bf3d644`
- Revert to master to persist Vn events and see the promotion from V1 ~> Vn and V2 ~> Vn
    - `git checkout master`

## Details ##
We configure Akka to perform serialization and event adapters via the configuration
```hocon
akka {
  persistence {
    journal {
      plugin = "akka.persistence.journal.leveldb"
      leveldb {
        event-adapters {
          shoppingCartAdapter = "com.experiments.calvin.eventadapters.ShoppingCartEventAdapter"
        }

        event-adapter-bindings {
          // V1 ~> V3
          "com.experiments.calvin.models.ShoppingCartV1" = shoppingCartAdapter
          // V2 ~> V3
          "com.experiments.calvin.models.ShoppingCartV2" = shoppingCartAdapter
        }
      }
    }
  }

  actor {
    serializers {
      shoppingCart = "com.experiments.calvin.serialization.ShoppingCartSerializer"
    }

    serialization-bindings {
      "com.experiments.calvin.models.ShoppingCartV1" = shoppingCart
      "com.experiments.calvin.models.ShoppingCartV2" = shoppingCart
      "com.experiments.calvin.models.ShoppingCartV3" = shoppingCart
    }
  }
}
```
Notice the event adapter configuration is placed within the journal section. In this case, 
we use the LevelDB journal so we place our Event Adapter configuration in the LevelDB 
section.

For this example, we choose to use a Shopping Cart. We evolve the Shopping
Cart over time and we need to deal with the old events persisted in the
journal and promote them to new version. We add a description field to
each of the items which in turn requires a new version of the Shopping Cart.
Also we update the name of one the fields.

We use the serializer to serialize and deserialize different versions of
the Shopping Cart. The Event Adapter handles the promotion of events from
V1 to V3, and from V2 to V3.


## Credits ##
- [Akka Schema Evolution](http://doc.akka.io/docs/akka/current/scala/persistence-schema-evolution.html)
- [Akka Serialization Test](https://github.com/dnvriend/akka-serialization-test)
- [BooPickle](https://github.com/ochrons/boopickle)
