package gd.eval.languages
import gd.eval.SandboxedLanguage

case class Ruby(code: String) extends SandboxedLanguage {
  val extension = "rb"
  val command = Seq("ruby", file.getAbsolutePath)
}
