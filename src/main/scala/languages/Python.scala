package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Python(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "py"
  val command = Seq("python", filename)
}
