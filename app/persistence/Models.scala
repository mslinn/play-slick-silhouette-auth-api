package persistence

import model.core.User.UserState

final case class DbLoginInfo(id: Long, userUuid: String, providerId: String, providerKey: String)

final case class DbPasswordInfo(loginInfoId: Long,
  hasher: String,
  password: String,
  salt: Option[String])

// todo remove dbUser as its same
final case class DbUser(uuid: String, // TODO: to uuid
                      email: String,
                      firstName: String,
                      lastName: String,

                      state: UserState)

