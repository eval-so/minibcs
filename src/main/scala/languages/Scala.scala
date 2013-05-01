package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Scala(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "scala"
  override val compileCommand = Some(Seq("scalac", filename))
  val command = Seq("scala", "EvalSO")
  override val timeout = 10
}
