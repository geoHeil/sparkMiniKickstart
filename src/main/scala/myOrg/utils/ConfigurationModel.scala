// Copyright (C) 2017-2018 geoHeil

package myOrg.utils

case class ConfigurationInvalidException(
  private val message: String = "",
  private val cause: Throwable = None.orNull)
  extends Exception(message, cause)

sealed case class SampleConfig(foo: String) {
  require(foo.length > 0, "At least some content must be there")
}