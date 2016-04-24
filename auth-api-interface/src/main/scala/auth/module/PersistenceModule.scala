package auth.module

import auth.persistence._
import auth.persistence.model.DbAccess
import auth.persistence.model.dao.impl.{LoginInfoDaoImpl, PasswordInfoDaoImpl, UserDaoImpl}
import auth.persistence.model.dao.{LoginInfoDao, PasswordInfoDao, UserDao}
import com.google.inject.{AbstractModule, Inject}
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import net.codingwell.scalaguice.ScalaModule
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.Effect.Schema

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
