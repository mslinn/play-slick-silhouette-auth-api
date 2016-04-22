package persistence

import com.mohiva.play.silhouette.api.LoginInfo
import model.core.User.UserState

trait DbTablesDefinitions {
  protected val driver: DbProfile
  import driver.api._

  sealed class UserMapping(tag: Tag) extends Table[DbUser](tag, "users") {
    def uuid: Rep[String] = column[String]("uuid", O.PrimaryKey)
    def email: Rep[String] = column[String]("email")
    def firstName: Rep[String] = column[String]("firstname")
    def lastName: Rep[String] = column[String]("lastname")
    def state: Rep[UserState] = column[UserState]("state")

    def * = (uuid, email, firstName, lastName, state) <> ((DbUser.apply _).tupled, DbUser.unapply)
  }

  sealed class LoginInfoMapping(tag: Tag) extends Table[DbLoginInfo](tag, "logininfo") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userUuid: Rep[String] = column[String]("user_uuid")
    def providerId: Rep[String] = column[String]("providerid")
    def providerKey: Rep[String] = column[String]("providerkey")

    foreignKey("fk_user_uuid", userUuid, usersQuery)(_.uuid)

    def * = (id, userUuid, providerId, providerKey) <> ((DbLoginInfo.apply _).tupled, DbLoginInfo.unapply)
  }

  sealed class PasswordInfoMapping(tag: Tag) extends Table[DbPasswordInfo](tag, "passwordinfo") {
    def hasher = column[String]("hasher")
    def password = column[String]("password")
    def salt = column[Option[String]]("salt")
    def loginInfoId = column[Long]("loginInfoId")

    foreignKey("fk_logininfo_id", loginInfoId, loginInfosQuery)(_.id)

    def * = (loginInfoId, hasher, password, salt) <> (DbPasswordInfo.tupled, DbPasswordInfo.unapply)
  }


  val usersQuery = TableQuery[UserMapping]

  val loginInfosQuery = TableQuery[LoginInfoMapping]

  def findDbLoginInfo(loginInfo: LoginInfo): Query[LoginInfoMapping, DbLoginInfo, Seq] =
    loginInfosQuery.filter(db => db.providerId === loginInfo.providerID && db.providerKey === loginInfo.providerKey)

  val passwordInfosQuery = TableQuery[PasswordInfoMapping]
}
