package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class `C++`(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "cxx"
  override val compileCommand = Some(Seq("g++", "-Wall", filename))
  val command = Seq("./a.out")
}
