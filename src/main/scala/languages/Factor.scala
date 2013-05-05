package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Factor(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "factor"
  val command = Seq("factor-vm", filename)
}
