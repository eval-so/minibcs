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

  /** LEGACY: Return a subclass of [[SandboxedLanguage]], configured to run an eval. */
  def route(language: String, code: String) = languages.get(language).map(_(EvaluationRequest(code)))

  /** Return a subclass of [[SandboxedLanguage]], configured to run an eval via Akka.
    *
    * Using this method, you can get an instance of a class that extends
    * [[SandboxedLanguage]]. This is useful to prepare a message to send to the
    * [[Router]] actor defined below.
    *
    * All new clients implementing BCS should use this method as opposed to
    * route[String, String](), even if they just call `.evaluate()` on the
    * resulting class.
    */
  def route(language: String, evaluation: EvaluationRequest) = languages.get(language).map(_(evaluation))
}

class Router extends Actor {
  def receive = {
    case s: SandboxedLanguage => sender ! s.evaluate
  }
}
