name := "sparkMiniSample"
organization := "myOrg"

scalaVersion := "2.11.12"

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
  "-Xlint:missing-interpolator",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-Ywarn-unused"
)

//The default SBT testing java options are too small to support running many of the tests
// due to the need to launch Spark in local mode.
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")
parallelExecution in Test := false

lazy val spark = "2.2.0"
lazy val pureconfigVersion = "0.8.0"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
//resolvers += Resolver.mavenLocal
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % spark % "provided",
  "org.apache.spark" %% "spark-sql" % spark % "provided",
  "org.apache.spark" %% "spark-hive" % spark % "provided",
  //  "graphframes" % "graphframes" % "0.5.0-spark2.1-s_2.11",
  //  "org.apache.spark" %% "spark-graphx" % spark % "provided",
  //  "org.apache.spark" %% "spark-mllib" % spark % "provided",
  //  "org.apache.spark" %% "spark-streaming" % spark % "provided",

  //  typesafe configuration
  "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,

  // testing
  "com.holdenkarau" %% "spark-testing-base" % s"${spark}_0.8.0" % "test"
)

fork := true
fullClasspath in reStart := (fullClasspath in Compile).value
run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass.in(Compile, run), runner.in(Compile, run)).evaluated

// outlining some dummy assembly configuration
assemblyMergeStrategy in assembly := {
  //  case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("rootdoc.txt") => MergeStrategy.discard
  case _ => MergeStrategy.deduplicate
}

//assemblyShadeRules in assembly := Seq(ShadeRule.rename("shapeless.**" -> "new_shapeless.@1").inAll)
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("shapeless.**" -> "shadeshapless.@1")
    .inLibrary("com.chuusai" % "shapeless_2.11" % "2.3.2")
    .inLibrary("com.github.pureconfig" % "pureconfig_2.11" % pureconfigVersion)
    .inLibrary("com.github.pureconfig" % "pureconfig-macros_2.11" % pureconfigVersion)
    .inProject
)

//test in assembly := {}

initialCommands in console :=
  """
    |import myOrg.utils.SparkBaseRunner
    |import org.slf4j.LoggerFactory
    |import org.apache.spark.sql.{ DataFrame, SparkSession }
    |import org.apache.spark.sql.functions._
    |import myOrg.utils.{SampleConfig, ConfigurationUtils}
    |
    |val logger = LoggerFactory.getLogger(this.getClass)
    |val c = ConfigurationUtils.loadConfiguration[SampleConfig]
    |val spark = ConfigurationUtils.createSparkSession("console")
    |import spark.implicits._
  """.stripMargin

mainClass := Some("myOrg.app.SparkJob")
