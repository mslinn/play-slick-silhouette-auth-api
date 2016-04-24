package auth.module

import auth.persistence._
import auth.persistence.model.dao.impl.{ LoginInfoDaoImpl, PasswordInfoDaoImpl, UserDaoImpl }
import auth.persistence.model.dao.{ LoginInfoDao, PasswordInfoDao, UserDao }
import auth.persistence.model.{ AuthDatabaseConfigProvider, AuthDbAccess}
import com.google.inject.{ AbstractModule, Inject, Provides }
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import net.codingwell.scalaguice.ScalaModule
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.backend.DatabaseConfig
import slick.dbio.Effect.Schema
import slick.profile.BasicProfile

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

  /**
    * Provides functionality to avoid spreading NamedDatabase across codebase
 *
    * @return config provider for auth database
    */
  @Provides
  def provideAuthDatabaseConfigProvider(@NamedDatabase("auth") dbConfigProvider: DatabaseConfigProvider): AuthDatabaseConfigProvider =
    new AuthDatabaseConfigProvider {
      override def get[P <: BasicProfile]: DatabaseConfig[P] = dbConfigProvider.get
    }
}

// TODO: dependant path to package obj
// TODO: @namedDb remove
class InitInMemoryDb @Inject() (protected val dbConfigProvider: AuthDatabaseConfigProvider) extends AuthDbAccess {
  import driver.api._

  // todo
  import scala.concurrent.ExecutionContext.Implicits.global

  private val f: DBIOAction[Unit, NoStream, Schema] = for {
    _ ← usersQuery.schema.create
    _ ← loginInfosQuery.schema.create
    _ ← passwordInfosQuery.schema.create
  } yield ()

  Await.ready(db.run(f.transactionally), 10.seconds)
  println("Tables created")
}
