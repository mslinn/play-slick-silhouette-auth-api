package controllers

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import model.core.User
import model.core.UserToken.TokenAction
import model.exchange.{Bad, Good, SignUp}
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsError, JsString, JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import service.{UserService, UserTokenService}
import utils.auth.DefaultEnv

import scala.concurrent.Future

/**repository
  * Sign up user to the system
  */
class SignUpController @Inject() (silhouette: Silhouette[DefaultEnv],
                                  passwordHasher: PasswordHasher,
                                  translate: MessagesApi,
                                  userService: UserService,
                                  userTokenService: UserTokenService,
                                  authInfoRepository: AuthInfoRepository) extends Controller with ResponseHelpers {

  import formatting.exchange.rest._
  import play.api.libs.concurrent.Execution.Implicits._

  /**
    * Registers users in service and issues token in backend
    */
  def signUpRequestRegistration: Action[JsValue] = Action.async(parse.json) { implicit request ⇒
    request.body.validate[SignUp].map { signUp ⇒
      val loginInfo = LoginInfo(CredentialsProvider.ID, signUp.identifier)

      userService.retrieve(loginInfo).flatMap {
        case Some(user) ⇒
          Future.successful(BadRequest(Json.toJson(Bad(message = translate("user.exists")))))

        case None ⇒
          val authInfo = passwordHasher.hash(signUp.password)
          val user = User(UUID.randomUUID.toString, loginInfo, signUp.identifier, signUp.firstName, signUp.lastName,
            User.State.Created)

          for {
            user ← userService.save(user)
            authInfo ← authInfoRepository.add(loginInfo, authInfo)
            registrationToken ← userTokenService.issue(user.uuid, TokenAction.ActivateAccount)
          } yield {
            // TODO: remove token from here, do not return it, so users have to visit email
            Ok(Json.toJson(Good(registrationToken.token)))
          }
      }
    }.recoverTotal(badRequestWithMessage)
  }
}
