package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class JRuby18(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "rb"
  val command = Seq("jruby", "--headless", "--1.8", filename)
}

case class JRuby19(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "rb"
  val command = Seq("jruby", "--headless", "--1.9", filename)
}
