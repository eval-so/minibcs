package so.eval.languages
import so.eval.SandboxedLanguage

case class `C++`(code: String) extends SandboxedLanguage {
  val extension = "cxx"
  override val compileCommand = Some(Seq("g++", "-Wall", filename))
  val command = Seq("./a.out")
}
