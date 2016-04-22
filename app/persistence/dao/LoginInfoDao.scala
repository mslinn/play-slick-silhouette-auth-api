package persistence.dao

import com.mohiva.play.silhouette.api.LoginInfo

import scala.concurrent.Future

trait LoginInfoDao {
  def save(loginInfo: LoginInfo, userUuid: String): Future[Unit]
}
