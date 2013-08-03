package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Elixir(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "exs"
  val command = Seq("elixir", filename)
}
