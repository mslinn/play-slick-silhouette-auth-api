package auth

import auth.persistence.drivers.AuthPostgresDriver

package object persistence {
  type DbProfile = AuthPostgresDriver // auth.persistence.drivers.H2Driver
  type SilhouetteLoginInfo = com.mohiva.play.silhouette.api.LoginInfo
  type SilhouettePasswordInfo = com.mohiva.play.silhouette.api.util.PasswordInfo
}
