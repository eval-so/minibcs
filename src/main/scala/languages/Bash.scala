package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Bash(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "bash"
  val command = Seq("bash", filename)
}
