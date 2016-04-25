package service.impl

import java.time.LocalDateTime
import java.util.UUID

import model.core.UserToken
import model.core.UserToken.UserTokenAction
import service.{ Hasher, UserTokenService }

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

// TODO: tokens not only im memory
class UserTokenServiceImpl extends UserTokenService {
  override def issue(userUuid: String, action: UserTokenAction): Future[UserToken] = ???

  override def claim(token: String): Future[Option[UserToken]] = ???
}

/**
  * Not thread safe. Usage in production is discouraged (as it needs to be singleton).
  */
class InMemoryUserTokenServiceImpl(hasher: Hasher) extends UserTokenService {

  val tokens: ArrayBuffer[UserToken] = ArrayBuffer.empty[UserToken]

  override def issue(userUuid: String, action: UserTokenAction): Future[UserToken] = {
    val tokenHash = hasher.hash(UUID.randomUUID.toString)

    // TODO: expiration days to config
    val t = UserToken(tokenHash, userUuid, LocalDateTime.now.plusDays(1), action)
    tokens += t
    Future.successful(t)
  }

  override def claim(token: String): Future[Option[UserToken]] = {
    val t = tokens.find(x ⇒ x.token == token)
    t.map(found ⇒ tokens -= found)
    Future.successful(t)
  }
}
