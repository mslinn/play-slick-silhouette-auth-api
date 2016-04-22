package persistence

import com.google.inject.{AbstractModule, Inject, Provides}
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import net.codingwell.scalaguice.ScalaModule
import persistence.model._
import persistence.model.dao.{LoginInfoDao, PasswordInfoDao, UserDao}
import persistence.model.dao.impl.{LoginInfoDaoImpl, PasswordInfoDaoImpl, UserDaoImpl}
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
  } yield ()

  Await.ready(db.run(f.transactionally), 10.seconds)
  println("Tables created")
}
