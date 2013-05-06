package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class LOLCODE(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "lol"
  val command = Seq("lci", filename)
}
