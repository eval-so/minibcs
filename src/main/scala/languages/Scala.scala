package gd.eval.languages
import gd.eval.SandboxedLanguage

case class Scala(code: String) extends SandboxedLanguage {
  val extension = "scala"
  override val compileCommand = Some(Seq("scalac", filename))
  val command = Seq("scala", "EvalGD")
}
