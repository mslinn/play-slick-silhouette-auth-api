package persistence.drivers

import model.core.User.State._
import model.core.User._
import slick.driver.JdbcDriver
import slick.jdbc.JdbcType

trait ModelMappingSupport {
 self: JdbcDriver#API =>
  implicit val stateMapper: JdbcType[UserState] = MappedColumnType.base[UserState, String]({
    case Created     ⇒ "created"
    case Activated   ⇒ "activated"
    case Deactivated ⇒ "deactivated"
  }, {
    case "created"     ⇒ Created
    case "activated"   ⇒ Activated
    case "deactivated" ⇒ Deactivated
  })
}
