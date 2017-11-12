package myOrg.utils

import com.typesafe.config.ConfigFactory
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.input.PortableDataStream
import org.apache.spark.sql.SparkSession

trait SparkBaseRunner extends App {

  @transient lazy val logger = LogManager.getLogger(this.getClass)
  @transient val conf = ConfigFactory.load

  def getOptionalString(path: String, defaultValue: String): String = if (conf.hasPath(path)) {
    conf.getString(path)
  } else {
    defaultValue
  }

  def createSparkSession(appName: String): SparkSession = {
    SparkSession
      .builder()
      .config(createConf(appName))
      //      .enableHiveSupport()
      .getOrCreate()
  }

  def createConf(appName: String): SparkConf = {
    new SparkConf()
      .setAppName(appName)
      .setIfMissing("spark.master", "local[*]")
      .setIfMissing("spark.driver.memory", "12G")
      .setIfMissing("spark.default.parallelism", "12")
      .setIfMissing("spark.driver.maxResultSize", "1G")
      .setIfMissing("spark.kryoserializer.buffer.max", "1G")
      .setIfMissing("spark.speculation", "true")
      .setIfMissing("spark.executor.extraJavaOptions", "-XX:+UseG1GC")
      .setIfMissing("spark.driver.extraJavaOptions", "-XX:+UseG1GC")
      .registerKryoClasses(Array(
        //TODO register all used classes here, force Kryo serialization and register all classes
        classOf[PortableDataStream]))
  }
}