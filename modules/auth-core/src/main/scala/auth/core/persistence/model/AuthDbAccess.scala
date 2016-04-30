package auth.core.persistence.model

import auth.core.persistence.AuthDbProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.backend.DatabaseConfig
import slick.profile.BasicProfile

/**
  * Mix in this driver if you are using dependency injection and require
  * Auth database with it's profile, i.e. Useful if you require only
  * single database
  * {{{
  * class MyService @Inject()(protected val dbConfigProvider: AuthDatabaseConfigProvider)
  *   extends AuthDbAccess {
  *     import driver.api._
  *   }
  * }}}
  */
trait AuthDbAccess extends HasDatabaseConfigProvider[AuthDbProfile]

/**
  * Provider that gets injected, i.e.
  * {{{
  * class MyService @Inject()(authDb: AuthDatabaseConfigProvider) {
  *   val (db, driver) = { val x = authDb.get[AuthDbProfile]; (x.db, x.driver) }
  * }
  * }}}
  * You can use it to get multiple different DBs in single instance
  */
trait AuthDatabaseConfigProvider extends DatabaseConfigProvider

trait AuthDatabaseDriver extends HasDbDriver[AuthDbProfile]

trait AuthDatabaseDriverConfigProvider extends HasDbDriverConfigProvider[AuthDbProfile]

trait HasDbDriver[P <: BasicProfile] {
  /** The Slick database configuration. */
  protected val dbConfig: DatabaseConfig[P] // field is declared as a val because we want a stable identifier.
  /** The Slick driver extracted from `dbConfig`. */
  protected final lazy val driver: P = dbConfig.driver // field is lazy to avoid early initializer problems.
}

trait HasDbDriverConfigProvider[P <: BasicProfile] extends HasDbDriver[P] {
  /** The provider of a Slick `DatabaseConfig` instance.*/
  protected val dbConfigProvider: DatabaseConfigProvider
  override final protected val dbConfig: DatabaseConfig[P] = dbConfigProvider.get[P]
}

