package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Smalltalk(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "st"
  val command = Seq("gst", filename)
}
