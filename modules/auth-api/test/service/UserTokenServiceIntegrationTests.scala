package service

import java.time.LocalDateTime
import java.util.UUID

import auth.api.model.core.UserToken
import testkit.util.IntegrationTest

class UserTokenServiceIntegrationTests extends IntegrationTest {

  "UserToken" should {
    "add token to database when issued" ignore {
      val token = UserToken("some-token", UUID.randomUUID, LocalDateTime.MIN, UserToken.TokenAction.ActivateAccount)


    }

    "return and remove token from database when claimed" ignore {

    }
  }


  trait Fixture {}
}
