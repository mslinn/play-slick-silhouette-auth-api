package persistence.model.dao

import persistence.model.SilhouetteLoginInfo

import scala.concurrent.Future

trait LoginInfoDao {
  def save(loginInfo: SilhouetteLoginInfo, userUuid: String): Future[Unit]
}
