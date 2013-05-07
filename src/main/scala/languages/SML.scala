package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class SML(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "sml"
  override val compileCommand = Some(Seq("mlton", "-output", "a.out", filename))
  val command = Seq("./a.out")
  override val timeout = 10
}
