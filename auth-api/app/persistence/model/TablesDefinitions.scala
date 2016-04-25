package persistence.model

import java.time.LocalDateTime

import auth.persistence.AuthDbProfile
import model.core.UserToken
import model.core.UserToken.UserTokenAction
import slick.lifted.ProvenShape

trait TablesDefinitions {
  protected val driver: AuthDbProfile
  import driver.api._

  sealed class UserTokenMapping(tag: Tag) extends Table[UserToken](tag, "usertokens") {
    def token: Rep[String] = column[String]("token", O.PrimaryKey)
    def userUuid: Rep[String] = column[String]("useruuid")
    def expiresOn: Rep[LocalDateTime] = column[LocalDateTime]("expireson")
    def tokenAction: Rep[UserTokenAction] = column[UserTokenAction]("tokenaction")

    override def * : ProvenShape[UserToken] =
      (token, userUuid, expiresOn, tokenAction) <> ((UserToken.apply _).tupled, UserToken.unapply)
  }
}
