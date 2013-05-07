package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class FSharp(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "fs"
  override lazy val filename = s"program.${extension}"
  override val compileCommand = Some(Seq("fsharpc", "--nologo", "-o", "a.exe", filename))
  val command = Seq("mono", "a.exe")
  override val timeout = 10
}
