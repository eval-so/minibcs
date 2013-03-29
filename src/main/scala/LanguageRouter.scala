package gd.eval
import gd.eval.languages._

object Router {
  def route(language: String, code: String) = language match {
    case "ruby" | "mri" => Some(Ruby(code))
    case "scala" => Some(Scala(code))
    case "c" => Some(C(code))
    case _ => None
  }
}
