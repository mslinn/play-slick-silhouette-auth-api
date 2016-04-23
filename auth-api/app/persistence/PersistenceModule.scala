package persistence

import com.google.inject.{AbstractModule, Inject, Provides}
import com.mohiva.play.silhouette.api.repositories.AuthenticatorRepository
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.CacheAuthenticatorRepository
import net.codingwell.scalaguice.ScalaModule
import persistence.model._
import persistence.model.dao.{LoginInfoDao, PasswordInfoDao, UserDao}
import persistence.model.dao.impl.{JwtAuthRepoImpl, LoginInfoDaoImpl, PasswordInfoDaoImpl, UserDaoImpl}
import persistence.drivers.AuthPostgresDriver
import persistence.model.DbAccess
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.dbio.Effect.Schema
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration._

sealed class PersistenceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserDao].to[UserDaoImpl]
    bind[LoginInfoDao].to[LoginInfoDaoImpl]
    bind[PasswordInfoDao].to[PasswordInfoDaoImpl]
    bind[InitInMemoryDb].asEagerSingleton // only for in memory db, create tables at start

    // For silhouette
    bind[DelegableAuthInfoDAO[SilhouettePasswordInfo]].to[PasswordInfoDaoImpl]
    //bind[AuthenticatorRepository[JWTAuthenticator]].to[CacheAuthenticatorRepository[JWTAuthenticator]] // todo: swap for db one // todo fix hardcoded jwt?
    //bind[AuthenticatorRepository[JWTAuthenticator]].to[FakeJwtAuthRepoImpl].asEagerSingleton // todo: swap for db one // todo fix hardcoded jwt?
    bind[AuthenticatorRepository[JWTAuthenticator]].to[JwtAuthRepoImpl] // todo: swap for db one // todo fix hardcoded jwt?
  }
}

// TODO: dependant path to package obj
class InitInMemoryDb @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends DbAccess {
  import driver.api._
  import play.api.libs.concurrent.Execution.Implicits._

  private val f: DBIOAction[Unit, NoStream, Schema] = for {
    _ ← usersQuery.schema.create
    _ ← loginInfosQuery.schema.create
    _ ← passwordInfosQuery.schema.create
    _ ← jwtAuthenticatorsQuery.schema.create
  } yield ()

  Await.ready(db.run(f.transactionally), 10.seconds)
  println("Tables created")
}
