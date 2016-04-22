package persistence.dao.impl

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import persistence.dao.LoginInfoDao
import persistence.{ DbLoginInfo, SlickAccess }
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class LoginInfoDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends LoginInfoDao with SlickAccess {

  import driver.api._
  import play.api.libs.concurrent.Execution.Implicits._

  //override def find(loginInfo: LoginInfo): Future[Option[LoginInfo]] = ???
  override def save(loginInfo: LoginInfo, userUuid: String): Future[Unit] = {
    val act = for {
      _ ← loginInfosQuery += DbLoginInfo(-1, userUuid, loginInfo.providerID, loginInfo.providerKey)
    } yield ()

    dbConfig.db.run(act)
  }
}
