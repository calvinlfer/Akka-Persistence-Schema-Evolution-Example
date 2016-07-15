name := "AkkaPersistenceEventAdapterSchemaEvolution"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion       = "2.4.8"
  Seq(
    // Akka
    "com.typesafe.akka"           %% "akka-actor"       % akkaVersion,
    "com.typesafe.akka"           %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka"           %% "akka-testkit"     % akkaVersion   % "test",
    "com.typesafe.akka"           %% "akka-slf4j"       % akkaVersion,

    // Local journal (Akka Persistence)
    // http://doc.akka.io/docs/akka/2.4.1/scala/persistence.html#Local_LevelDB_journal
    "org.iq80.leveldb"            % "leveldb"          % "0.7",
    "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",

    // Commons IO is needed for cleaning up data when testing persistent actors
    "commons-io"                  %  "commons-io"       % "2.4",
    "ch.qos.logback"              %  "logback-classic"  % "1.1.3",
    "org.scalatest"               %% "scalatest"        % "2.2.6"       % "test",

    // Scala Pickling library
    "me.chrons"                   %% "boopickle"        % "1.2.4"

  )
}