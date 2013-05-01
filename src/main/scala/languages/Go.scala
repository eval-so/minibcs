package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

case class Go(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "go"
  val allFiles = filename :: evaluation.files.map {
    _.keys.filter(f => f.endsWith(".go")).toList
  }.getOrElse(List())
  override val compileCommand = Some(Seq("go", "build") ++ allFiles)
  val command = Seq(s"./${filename.replaceAll("\\.go$", "")}")
}
