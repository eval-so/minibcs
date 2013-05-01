package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Io(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "io"
  val command = Seq("io", filename)
}
