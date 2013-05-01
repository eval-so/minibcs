package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Ruby(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "rb"
  val command = Seq("ruby", filename)
}
