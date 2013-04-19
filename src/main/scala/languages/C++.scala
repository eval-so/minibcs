package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class `C++`(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "cxx"
  val allFiles = filename :: evaluation.files.map {
    _.keys.filter(f => f.endsWith(".cpp") || f.endsWith(".cxx")).toList
  }.getOrElse(List())
  override val compileCommand = Some(Seq("g++", "-Wall", allFiles.mkString(" ")))
  val command = Seq("./a.out")
}
