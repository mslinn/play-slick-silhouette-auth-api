package auth.module

import auth.service.impl._
import auth.service.{Hasher, UserService}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

sealed class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Hasher].to[Sha1HasherImpl]

    bind[UserService].to[UserServiceImpl]
  }
}
