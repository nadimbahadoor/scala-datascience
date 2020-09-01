name := "spark-nlp"

version := "1.0"

scalaVersion := "2.11.8"

logLevel := Level.Info

libraryDependencies ++= Seq(
  "org.apache.spark"           %% "spark-core"      % "2.0.1" % "provided",
  "org.apache.spark"           %% "spark-mllib"     % "2.0.1",
  "org.apache.commons"         %  "commons-lang3"   % "3.3.2",
  "com.typesafe.play"          %  "play-json_2.10"  % "2.2.1",
  "ch.qos.logback"             %  "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.4.0"
)

resolvers ++= Seq(
  "apache-snapshots"    at "http://repository.apache.org/snapshots/",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)