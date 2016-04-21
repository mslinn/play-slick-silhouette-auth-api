package service

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.CacheLayer
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import model.core.User

import scala.concurrent.Future

/**
  * Handles actions to users
  */
trait UserService extends IdentityService[User] {
  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save(profile: CommonSocialProfile): Future[User]

  /**
    * Sets new state to user with `userUuid`
    * @return true if new state was set successfuly, otherwise false
    */
  def setState(userUuid: String, newState: User.UserState): Future[Boolean]

  def retrieve(userUuid: String): Future[Option[User]]
}
