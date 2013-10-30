package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Idris(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "idr"
  override lazy val filename = s"program.${extension}"
  override val compileCommand = Some(Seq("idris", "-o", "program", filename))
  val command = Seq("./program")
}
