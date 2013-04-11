package so.eval
import so.eval.languages._

import akka.actor.Actor

object Router {
  /* The list of languages we can evaluate. Keep alphabetical. */
  val languages = Map(
    "c" -> C,
    "c++" -> `C++`,
    "python" -> Python,
    "ruby" -> Ruby,
    "scala" -> Scala
  )

  def route(language: String, code: String) = languages.get(language).map(_(code))
}

class Router extends Actor {
  def receive = {
    case s: SandboxedLanguage => sender ! s.evaluate
  }
}
