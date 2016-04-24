package auth.service

import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import auth.service.impl._

sealed class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Hasher].to[Sha1HasherImpl]

    bind[UserService].to[UserServiceImpl]
  }
}
