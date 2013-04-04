package so.eval.languages
import so.eval.SandboxedLanguage

case class Ruby(code: String) extends SandboxedLanguage {
  val extension = "rb"
  val command = Seq("ruby", filename)
}
