package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

trait JRuby extends SandboxedLanguage {
  val extension = "rb"
  override val timeout = 10
}

case class JRuby18(evaluation: EvaluationRequest) extends JRuby {
  val command = Seq("jruby", "--headless", "--1.8", filename)
}

case class JRuby19(evaluation: EvaluationRequest) extends JRuby {
  val command = Seq("jruby", "--headless", "--1.9", filename)
}
