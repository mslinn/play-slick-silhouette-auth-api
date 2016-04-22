package persistence.model

import model.core.User.UserState

// todo remove dbUser as its same
final case class DbUser(uuid: String, // TODO: to uuid
                      email: String,
                      firstName: String,
                      lastName: String,

                      state: UserState)

