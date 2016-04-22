package persistence.dao.impl

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import persistence._
import persistence.dao.PasswordInfoDao
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

// todo: is acutally servis not persistence.mapping.dao/repo
class PasswordInfoDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends PasswordInfoDao with SlickAccess {

  import driver.api._
  import play.api.libs.concurrent.Execution.Implicits._

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val query = passwordInfoQuery(loginInfo).result.headOption
    db.run(query).map { dbPasswordInfoOpt ⇒
      dbPasswordInfoOpt.map(dbPasswordInfo ⇒
        PasswordInfo(dbPasswordInfo.hasher, dbPasswordInfo.password, dbPasswordInfo.salt))
    }
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    db.run(updateAction(loginInfo, authInfo)).map(_ => authInfo)

  override def remove(loginInfo: LoginInfo): Future[Unit] =
    db.run(passwordInfoSubQuery(loginInfo).delete).map(_ => ())

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val query = findDbLoginInfo(loginInfo).joinLeft(passwordInfosQuery)
        .on(_.id === _.loginInfoId)

    // todo addorupdate slick?
    val action = query.result.head.flatMap {
      case (dbLoginInfo, Some(dbPasswordInfo)) => updateAction(loginInfo, authInfo)
      case (dbLoginInfo, None) => addAction(loginInfo, authInfo)
    }

    db.run(action).map(_ => authInfo)
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    db.run(addAction(loginInfo, authInfo)).map(_ => authInfo)

  protected def passwordInfoQuery(loginInfo: LoginInfo) = for {
    dbLoginInfo ← findDbLoginInfo(loginInfo)
    dbPasswordInfo ← passwordInfosQuery if dbPasswordInfo.loginInfoId === dbLoginInfo.id
  } yield dbPasswordInfo

  protected def passwordInfoSubQuery(loginInfo: LoginInfo) =
    passwordInfosQuery.filter(_.loginInfoId in findDbLoginInfo(loginInfo).map(_.id))

  protected def addAction(loginInfo: LoginInfo, authInfo: PasswordInfo) =
    findDbLoginInfo(loginInfo).result.head.flatMap { dbLoginInfo ⇒
      passwordInfosQuery +=
        DbPasswordInfo(dbLoginInfo.id, authInfo.hasher, authInfo.password, authInfo.salt)
    }

  private def updateAction(loginInfo: LoginInfo, authInfo: PasswordInfo) =
    passwordInfoSubQuery(loginInfo)
      .map(dbPasswordInfo ⇒ (dbPasswordInfo.hasher, dbPasswordInfo.password, dbPasswordInfo.salt))
      .update((authInfo.hasher, authInfo.password, authInfo.salt))
}
