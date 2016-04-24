package auth.persistence.model.dao.impl

import auth.model.core.User
import com.google.inject.Inject
import com.mohiva.play.silhouette
import User.UserState
import auth.persistence.model.{DbAccess, DbUser}
import auth.persistence.model.dao.UserDao
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase

import scala.concurrent.Future

// TODO: should not run queries, should only prepare them for services, instead of full dbconfig, get just api
class UserDaoImpl @Inject()
  (@NamedDatabase("auth") protected val dbConfigProvider: DatabaseConfigProvider)
  extends UserDao with DbAccess {

  import driver.api._
  import play.api.libs.concurrent.Execution.Implicits._
  println("user dao init")

  override def find(loginInfo: silhouette.api.LoginInfo): Future[Option[User]] = {
    println("finding ", loginInfo)
    val userQuery = for {
    (loginInfo, user) ← findDbLoginInfo(loginInfo)
        .join(usersQuery).on(_.userUuid === _.uuid)
    } yield user

    dbConfig.db.run(userQuery.result.headOption).map { dbUserOption ⇒
      dbUserOption.map { user ⇒
        User(user.uuid, user.email, user.firstName, user.lastName, user.state)
      }
    }
  }

  override def find(userUuid: String): Future[Option[User]] = {
    println("finging", userUuid)
    val query = usersQuery.filter(_.uuid === userUuid)
    dbConfig.db.run(query.result.headOption)
      .map { opt =>
        opt.map { u =>
          User(u.uuid, u.email, u.firstName, u.lastName, u.state)
        }
      }
  }

  override def save(user: User): Future[User] = {
    println("saving", user)
    val dbUser = DbUser(user.uuid, user.email, user.firstName, user.lastName, user.state)

    val act = for {
      _ ← usersQuery.insertOrUpdate(dbUser)
    } yield ()

    dbConfig.db.run(act).map(_ ⇒ user)
  }

  override def setState(userUuid: String, newState: UserState): Future[Boolean] = {
   val act =  usersQuery
      .filter(_.uuid === userUuid)
      .map(_.state)
      .update(newState)

    dbConfig.db.run(act).map(amountChanged => amountChanged != 0)
  }
}
