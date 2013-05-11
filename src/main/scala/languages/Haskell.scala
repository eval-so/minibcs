package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Haskell(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "hs"
  override lazy val filename = s"program.${extension}"
  override val compileCommand = Some(Seq("ghc", filename))
  val command = Seq("./program")
}
