lazy val VERSION = "0.5.1"
Global / onChangedBuildSource := ReloadOnSourceChanges

def commonSettings(_name: String) = Seq(
  name := _name,
  organization := "net.petitviolet",
  version := VERSION,
  crossScalaVersions := Seq("2.11.12", "2.12.12", "2.13.10"),

  libraryDependencies ++= {
    val isScala3 = scalaVersion.value.startsWith("3")
    val stVer = if (isScala3) "3.2.15" else "3.0.8"
    val scVer = if (isScala3) "1.15.4" else "1.14.0"
    if (isScala3)
      Seq("org.scalatestplus" %% "scalacheck-1-17" % stVer % Test)
    else
      Seq(
        "org.scalatest" %% "scalatest" % stVer % Test,
        "org.scalacheck" %% "scalacheck" % scVer % Test,
      )
  },
  scalacOptions ++= {
    val isScala3 = scalaVersion.value.startsWith("3")
    val isScala2_13 = scalaVersion.value.startsWith("2.13")
    if (isScala3) {
      Seq("-deprecation", "-feature", "-Werror", "-release:11")
    } else if (isScala2_13) {
      Seq("-Werror", "-deprecation", "-release:11", "-Xsource:3",
        "-Wdead-code", "-feature", "-Werror")
    } else Seq.empty[String]
  },
  scalafmtSbtCheck := true,
  scalafmtConfig := Some(file(".scalafmt.conf")),
  scalafmtOnCompile := true,
)

lazy val root = (project in file("."))
  .settings(commonSettings("ulid4s-root"))
  .aggregate(ulid4s)

lazy val ulid4s = (project in file("ulid4s"))
  .settings(commonSettings("ulid4s"))
  .settings(
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    Test / testOptions += Tests.Argument(
      TestFrameworks.ScalaTest, "-u", {
        val dir = System.getenv("CI_REPORTS")
        if(dir == null) "target/reports" else dir
      })
  )

lazy val benchmark = (project in file("benchmark"))
  .enablePlugins(JmhPlugin)
  .settings(commonSettings("benchmark"))
  .dependsOn(ulid4s)


