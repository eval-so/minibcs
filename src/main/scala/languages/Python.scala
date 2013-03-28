package gd.eval.languages
import gd.eval.SandboxedLanguage

case class Python(code: String) extends SandboxedLanguage {
  val extension = "py"
  val command = Seq("python", file.getAbsolutePath)
}
