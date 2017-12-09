// Copyright (C) 2017-2018 geoHeil

package myOrg.utils

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

trait SparkBaseRunner extends App {

  @transient lazy val logger = LoggerFactory.getLogger(this.getClass)
  val c = ConfigurationUtils.loadConfiguration[SampleConfig]

  def createSparkSession(appName: String): SparkSession = {
    ConfigurationUtils.createSparkSession(appName)
  }
}