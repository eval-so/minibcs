package so.eval.languages
import so.eval.SandboxedLanguage

case class Python(code: String) extends SandboxedLanguage {
  val extension = "py"
  val command = Seq("python", filename)
}
