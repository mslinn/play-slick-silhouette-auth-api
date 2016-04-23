package persistence.model.dao.impl

import com.google.inject.Inject
import persistence.model._
import persistence.model.dao.LoginInfoDao
import persistence.model.{DbAccess, LoginInfo}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class LoginInfoDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends LoginInfoDao with DbAccess {

  println("login info dao initiated")

  import driver.api._
  import play.api.libs.concurrent.Execution.Implicits._

  override def save(loginInfo: SilhouetteLoginInfo, userUuid: String): Future[Unit] = {
    println("saving logininfo")
    val act = for {
      _ ← loginInfosQuery += LoginInfo(-1, userUuid, loginInfo.providerID, loginInfo.providerKey)
    } yield ()

    dbConfig.db.run(act)
  }
}
