package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Rust(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "rs"
  override val compileCommand = Some(Seq("rust", "build", "-o", "a.out", filename))
  val command = Seq("./a.out")
}
