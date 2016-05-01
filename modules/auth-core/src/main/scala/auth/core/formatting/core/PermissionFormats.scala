package auth.core.formatting.core

import auth.core.model.core.{ AccessAdmin, AccessBar, Permission }
import play.api.libs.json._

object PermissionFormats {

  val rest: Format[Permission] = new Format[Permission] {

    override def reads(json: JsValue): JsResult[Permission] = json match {
      case JsString(x) ⇒ Permission
        .fromString(x)
        .map(JsSuccess(_))
        .getOrElse(JsError(s"No state found for $json"))
      case _ ⇒ JsError(s"Can't parse $json to UserTokenAction")
    }

    override def writes(o: Permission): JsValue = JsString(o.toString)
  }
}
