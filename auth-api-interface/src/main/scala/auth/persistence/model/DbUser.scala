package auth.persistence.model

import auth.model.core.User
import User.UserState

// todo remove dbUser as its same
final case class DbUser(uuid: String, // TODO: to uuid
                      email: String,
                      firstName: String,
                      lastName: String,

                      state: UserState)

