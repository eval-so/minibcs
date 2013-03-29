package gd.eval
import gd.eval.languages._

object Router {
  def route(language: String, code: String) = language match {
    case "ruby" | "mri" => Ruby(code)
    case "scala" => Scala(code)
    case "c" => C(code)
  }
}
