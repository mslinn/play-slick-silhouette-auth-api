package persistence

package object model {
  // Small impedance in naming - our db classes do not represent needed auth classes
  type SilhouetteLoginInfo = com.mohiva.play.silhouette.api.LoginInfo
  type SilhouettePasswordInfo = com.mohiva.play.silhouette.api.util.PasswordInfo
}
