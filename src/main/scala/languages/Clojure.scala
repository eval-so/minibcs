package so.eval.languages
import so.eval.{EvaluationRequest, SandboxedLanguage}

case class Clojure(evaluation: EvaluationRequest) extends SandboxedLanguage {
  val extension = "clj"
  val command = Seq(
    "java",
    "-client",
    "-XX:+TieredCompilation",
    "-XX:TieredStopAtLevel=1",
    "-Xbootclasspath/a:/usr/share/java/clojure.jar",
    "clojure.main",
    filename)
  override val timeout = 10
}
