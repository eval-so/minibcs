package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class VBNET(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "vb"
  override lazy val filename = s"program.${extension}"
  override val compileCommand = Some(Seq("vbnc", "-nologo", filename))
  val command = Seq("mono", "program.exe")
}
