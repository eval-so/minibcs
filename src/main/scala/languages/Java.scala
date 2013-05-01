package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Java(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "java"
  override val compileCommand = Some(Seq("javac", filename))
  val command = Seq("java", "EvalSO")
  override lazy val filename = "EvalSO.java"
}
