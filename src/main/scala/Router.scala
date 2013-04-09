package so.eval
import so.eval.languages._

object Router {
  /* The list of languages we can evaluate. Keep alphabetical. */
  val languages = Map(
    "c" -> C,
    "python" -> Python,
    "ruby" -> Ruby,
    "scala" -> Scala
  )

  def route(language: String, code: String) = languages.get(language).map(_(code))
}
