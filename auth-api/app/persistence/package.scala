import persistence.drivers.AuthPostgresDriver

package object persistence {
  // Change if you wish to swap for different driver - though it will break at compile time
  // if unsupported functionality is required from driver
  type DbProfile = AuthPostgresDriver // persistence.drivers.H2Driver
}
