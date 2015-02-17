name := """wbi"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation"
)

libraryDependencies ++= Seq(
  javaEbean,
  javaWs,
  cache,
  filters
)

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.apache.httpcomponents" % "httpclient" % "4.3.6"
)

unmanagedJars in Compile <++= baseDirectory map { base =>
  val customJars = ((base / "lib") ** "*" ** "*.jar")
  customJars.classpath
}
