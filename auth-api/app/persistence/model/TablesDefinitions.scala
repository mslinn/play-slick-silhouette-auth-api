package persistence.model

import java.sql.Timestamp
import java.time.LocalDateTime

import auth.persistence.HasAuthDbProfile
import model.core.UserToken
import model.core.UserToken.UserTokenAction
import slick.lifted.ProvenShape

trait TablesDefinitions extends ModelMappingSupport with HasAuthDbProfile {
  import driver.api._

  sealed class UserTokenMapping(tag: Tag) extends Table[UserToken](tag, "usertokens") {
    def token: Rep[String] = column[String]("token", O.PrimaryKey)
    def userUuid: Rep[String] = column[String]("useruuid")
    //def expiresOn: Rep[LocalDateTime] = column[LocalDateTime]("expireson")
    def expiresOn: Rep[String] = column[String]("expireson")
    def tokenAction: Rep[UserTokenAction] = column[UserTokenAction]("tokenaction")


    def foo(x: UserToken): Option[(String, String, String, UserTokenAction)] =
      Some(x.token, x.userUuid, x.expiresOn.toString, x.tokenAction)
    def bar(x: (String, String, String, UserTokenAction)): UserToken =
      UserToken(x._1, x._2, LocalDateTime.parse(x._3), x._4)

    override def * : ProvenShape[UserToken] =
      (token, userUuid, expiresOn, tokenAction) <> (bar, foo)
      //(token, userUuid, expiresOn, tokenAction) <> ((UserToken.apply _).tupled, UserToken.unapply)
  }

  val userTokensQuery = TableQuery[UserTokenMapping]
}
