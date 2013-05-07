package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Lua(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "lua"
  val command = Seq("lua", filename)
}
