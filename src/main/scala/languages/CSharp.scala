package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class CSharp(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "cs"
  override lazy val filename = s"program.${extension}"
  override val compileCommand = Some(Seq("mcs", filename))
  val command = Seq("mono", "program.exe")
  override val timeout = 10
}
