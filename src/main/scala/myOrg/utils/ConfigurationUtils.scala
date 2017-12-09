// Copyright (C) 2017 geoHeil

package myOrg.utils

import com.typesafe.config.{ Config, ConfigFactory }
import org.apache.spark.SparkConf
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.SparkSession
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ValueReader
import net.ceedubs.ficus.readers.namemappers.implicits.hyphenCase

object ConfigurationUtils {

  def loadConfiguration[T: ValueReader](): T = {
    val config: Config = ConfigFactory.load()
    config.as[T]
  }

  def createSparkSession(appName: String): SparkSession = {
    SparkSession
      .builder()
      .config(createConf(appName))
      //  .enableHiveSupport()
      .getOrCreate()
  }

  def createConf(appName: String): SparkConf = {
    new SparkConf()
      .setAppName(appName)
      .setIfMissing("spark.master", "local[*]")
      .setIfMissing("spark.driver.memory", "12G")
      .setIfMissing("spark.default.parallelism", "12")
      .setIfMissing("spark.driver.maxResultSize", "1G")
      .setIfMissing("spark.speculation", "true")
      .setIfMissing("spark.serializer", classOf[KryoSerializer].getCanonicalName)
      .setIfMissing("spark.kryoserializer.buffer.max", "1G")
      .setIfMissing("spark.kryo.unsafe", "true")
      .setIfMissing("spark.kryo.referenceTracking", "false")
      .setIfMissing("spark.driver.extraJavaOptions", "-XX:+UseG1GC")
      .setIfMissing("spark.executor.extraJavaOptions", "-XX:+UseG1GC")
      .setIfMissing("spark.memory.offHeap.enabled", "true")
      .setIfMissing("spark.memory.offHeap.size", "1g")
      .setIfMissing("spark.yarn.executor.memoryOverhead", "1g")
    //  .registerKryoClasses(Array(
    //  //TODO register all used classes here, force Kryo serialization and register all classes
    //  classOf[PortableDataStream]))
  }

}
