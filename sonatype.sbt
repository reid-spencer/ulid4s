import xerial.sbt.Sonatype.GitHubHosting

sonatypeProfileName := "net.petitviolet"
organization := "net.petitviolet"

publishMavenStyle.withRank(KeyRanks.Invisible)  := true

val githubAccount = "petitviolet"
val githubProject = "ulid4s"

// Add sonatype repository settings
publishTo in Global := {
  if (isSnapshot.value)
    Opts.resolver.sonatypeOssSnapshots
  else
    Opts.resolver.sonatypeOssReleases
}.headOption

Global / licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

Global / sonatypeProjectHosting  := Some(GitHubHosting(githubAccount, githubProject, "mail@petitviolet.net"))

Global / homepage := Some(url(s"https://github.com/$githubAccount/$githubProject"))
Global / scmInfo := Some(ScmInfo(url(s"https://github.com/$githubAccount/$githubProject"),
                            s"git@github.com:$githubAccount/$githubProject.git"))
Global/ developers := List(Developer("petitviolet",
                             "Hiroki Komurasaki",
                             "mail@petitviolet.net",
                             url("https://www.petitviolet.net")))
