package auth.persistence.drivers

trait H2Driver extends slick.driver.H2Driver  {
  override val api: H2DriverApi.type = H2DriverApi

  object H2DriverApi extends API with ModelMappingSupport
}
