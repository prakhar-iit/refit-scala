name := "refit-scala"

version := "0.1"

scalaVersion := "2.12.7"

val flinkVersion = "1.7.0"
val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided")

lazy val root = project
  .in(file("."))
  .settings(
    name := "refit-scala",
    version := "0.1.0-SNAPSHOT",

    libraryDependencies ++= flinkDependencies
  )