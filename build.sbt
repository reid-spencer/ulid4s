lazy val VERSION = "0.5.1"
Global / onChangedBuildSource := ReloadOnSourceChanges

def commonSettings(_name: String) = Seq(
  name := _name,
  organization := "net.petitviolet",
  version := VERSION,
  scalaVersion := "3.2.2",
  crossScalaVersions := Seq("2.12.12", "2.13.10", "3.2.2"),

  libraryDependencies ++= {
    val isScala3 = scalaVersion.value.startsWith("3")
    val stVer = "3.2.15"
    val stPVer = stVer + ".0"
    if (isScala3)
      Seq(
        "org.scalatestplus" %% "scalacheck-1-17" % stPVer % Test,
        "org.scalatest" %% "scalatest" % stVer % Test,
      )
    else
      Seq(
        "org.scalatestplus" %% "scalacheck-1-17" % stPVer % Test,
        "org.scalatest" %% "scalatest" % stVer % Test,
      )
  },
  scalacOptions ++= {
    val isScala3 = scalaVersion.value.startsWith("3")
    val isScala2_13 = scalaVersion.value.startsWith("2.13")
    if (isScala3) {
      Seq("-deprecation", "-feature", "-Werror", "-release:11",
        "-rewrite", "-source","3.0-migration")
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


