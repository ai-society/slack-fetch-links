import play.ebean.sbt.PlayEbean

name := """slack-fetch-bot"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += jdbc
libraryDependencies += "org.postgresql"%"postgresql"%"9.2-1004-jdbc41"
libraryDependencies += evolutions
libraryDependencies += filters

lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
