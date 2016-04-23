package persistence.model.dao.impl

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.repositories.AuthenticatorRepository
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import persistence.model.DbAccess
import play.api.db.slick.DatabaseConfigProvider

import scala.collection.mutable
import scala.concurrent.Future

class JwtAuthRepoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends AuthenticatorRepository[JWTAuthenticator] with DbAccess {

  println("jwt auth initiated")

  import driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global
  import com.mohiva.play.silhouette.api.LoginInfo
  import org.joda.time.DateTime

  def find(id: String): Future[Option[JWTAuthenticator]] = {
    println("looking for", id)
    db.run(jwtAuthenticatorsQuery.filter(_.id === id).result.headOption)
  }

  def add(authenticator: JWTAuthenticator): Future[JWTAuthenticator] = {
    println("adding", authenticator)
    db.run(jwtAuthenticatorsQuery += authenticator).map(_ => authenticator)
  }

  def update(authenticator: JWTAuthenticator): Future[JWTAuthenticator] = {
    println("updating", authenticator)
    db.run(
      jwtAuthenticatorsQuery.filter(_.id === authenticator.id).update(authenticator))
      .map(_ => authenticator)
  }

  def remove(id: String): Future[Unit] = {
    println("removing", id)
    db.run(jwtAuthenticatorsQuery.filter(_.id === id).delete).map(_ => ())
  }
}


class FakeJwtAuthRepoImpl extends AuthenticatorRepository[JWTAuthenticator] {
  import com.mohiva.play.silhouette.api.LoginInfo
  import org.joda.time.DateTime


  type T = JWTAuthenticator

  var data: mutable.HashMap[String, T] = mutable.HashMap()

  def find(id: String): Future[Option[T]] = {
    println("looking for", id)
    val s = data.get(id)
    println("found", s)
    Future.successful(s)
  }

  def add(authenticator: T): Future[T] = {
    println("inserting", authenticator.id, authenticator)
    data += (authenticator.id -> authenticator)
    Future.successful(authenticator)
  }

  def update(authenticator: T): Future[T] = {
    println("updating", authenticator.id, authenticator)
    data += (authenticator.id -> authenticator)
    Future.successful(authenticator)
  }

  def remove(id: String): Future[Unit] = {
    println("removing", id)
    data -= id
    Future.successful(())
  }
}
