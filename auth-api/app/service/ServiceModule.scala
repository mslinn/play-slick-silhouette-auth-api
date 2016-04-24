package service

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import service.impl.InMemoryUserTokenServiceImpl

sealed class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserTokenService].to[InMemoryUserTokenServiceImpl].asEagerSingleton
  }
}
