package service.impl

import play.api.libs.Codecs
import service.Hasher

class Sha1HasherImpl extends Hasher {
  private[this] val md = java.security.MessageDigest.getInstance("SHA-1")

  override def hash(text: String): String =
    Codecs.sha1(md.digest(text.getBytes))
}
