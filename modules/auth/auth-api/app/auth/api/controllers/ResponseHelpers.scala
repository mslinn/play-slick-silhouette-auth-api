package auth.api.controllers

import auth.api.model.exchange.Bad
import play.api.libs.json.{Format, JsError, Json}
import play.api.mvc.{Result, Results}

import scala.concurrent.Future

trait ResponseHelpers { self: Results ⇒

  /**
    * @param err error to encode info from
    * @param badFormat format of response json
    */
  def badRequestWithMessage(err: JsError)(implicit badFormat: Format[Bad]): Future[Result] =
    Future.successful(BadRequest(Json.toJson(Bad("json.invalid", JsError.toJson(err)))))
}
