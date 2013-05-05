package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class ChickenScheme(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "scm"
  override val compileCommand = Some(Seq("csc", "-o", "a.out", filename))
  val command = Seq("./a.out")
}
