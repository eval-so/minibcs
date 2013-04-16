package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class C(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "c"
  override val compileCommand = Some(Seq("gcc", "-Wall", filename))
  val command = Seq("./a.out")
}
