import scalariform.formatter.preferences._

val akkaVersion = "2.5.17"
val redisScalaVersion = "1.8.4"

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  // The Nexus repo we're publishing to.
  publishTo := (version { (v: String) =>
    val nexus = "http://172.16.1.115:8081/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "repository/maven-snapshots")
    else Some("releases" at nexus + "repository/maven-releases")
  }).value,
  pomIncludeRepository := { x => false },
  pomExtra := (
    <developers>
      <developer>
        <id>satabin</id>
        <name>Lucas Satabin</name>
        <email>satabin@safety-data.com</email>
      </developer>
    </developers>
      <ciManagement>
        <system>travis</system>
        <url>https://travis-ci.org/#!/safety-data/akka-persistence-redis</url>
      </ciManagement>
      <issueManagement>
        <system>github</system>
        <url>https://github.com/safety-data/akka-persistence-redis/issues</url>
      </issueManagement>
    )
)

lazy val siteSettings = Seq(
  ghpagesNoJekyll := true,
  git.remoteRepo := scmInfo.value.get.connection)

lazy val dependencies = Seq(
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
  "com.github.Ma27" %% "rediscala" % redisScalaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-persistence-tck" % akkaVersion % "test",
  "com.github.pocketberserker" %% "scodec-msgpack" % "0.6.0" % "test")

lazy val root = project.in(file("."))
  .enablePlugins(SiteScaladocPlugin, GhpagesPlugin)
  .settings(publishSettings: _*)
  .settings(siteSettings: _*)
  .settings(
    resolvers += Resolver.mavenLocal,
    organization := "com.acloudchina.akka",
    name := "akka-persistence-redis",
    version := "1.0.0",
    licenses += ("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/safety-data/akka-persistence-redis")),
    scmInfo := Some(ScmInfo(url("https://github.com/safety-data/akka-persistence-redis"), "git@github.com:safety-data/akka-persistence-redis.git")),
    scalaVersion := "2.12.7",
    crossScalaVersions := Seq("2.12.7", "2.11.12"),
    libraryDependencies ++= dependencies,
    parallelExecution in Test := false,
    scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-implicits-show-all", "-diagrams", "-doc-title", "Akka Persistence Redis", "-doc-version", version.value, "-doc-footer", "Copyright © 2017 Safety Data"),
    autoAPIMappings := true,
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked"))
  .settings(
    scalariformPreferences := {
      scalariformPreferences.value
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(DoubleIndentConstructorArguments, true)
        .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
    })
