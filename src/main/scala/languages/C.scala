package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class C(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "c"
  val allFiles = filename :: evaluation.files.map {
    _.keys.filter(f => f.endsWith(".c")).toList
  }.getOrElse(List())
  override val compileCommand = Some(Seq("gcc", "-Wall", allFiles.mkString(" ")))
  val command = Seq("./a.out")
}
