package auth.persistence.model.dao.impl

import auth.persistence.SilhouetteLoginInfo
import com.google.inject.Inject
import auth.persistence.model._
import auth.persistence.model.dao.LoginInfoDao
import auth.persistence.model.{DbAccess, LoginInfo}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase

import scala.concurrent.Future

class LoginInfoDaoImpl @Inject() (@NamedDatabase("auth") protected val dbConfigProvider: DatabaseConfigProvider)
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
