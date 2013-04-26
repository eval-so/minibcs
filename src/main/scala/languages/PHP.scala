package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class PHP(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "php"
  val command = Seq("php", filename)
}
