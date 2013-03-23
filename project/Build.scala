import sbt._
import Keys._
import PlayProject._
import ScctPlugin._

object ApplicationBuild extends Build {

    val appName         = "peckham-foods"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1" % "test",
      "org.mockito" % "mockito-all" % "1.9.5" % "test",
      "org.scalatest" %% "scalatest" % "1.9.1" % "test"
    )

    lazy val additionalSettings = Defaults.defaultSettings ++ seq(ScctPlugin.instrumentSettings : _*)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA, settings = additionalSettings).settings(
       // Add your own project settings here
      parallelExecution in ScctPlugin.ScctTest := false,
      unmanagedResourceDirectories in ScctPlugin.ScctTest <+= baseDirectory( _ / "conf"),
      testOptions in Test := Nil
    )
}
