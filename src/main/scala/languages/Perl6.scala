package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Perl6(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "pl"
  val command = Seq("perl6", filename)
}
