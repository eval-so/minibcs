package so.eval.languages
import so.eval.SandboxedLanguage

case class C(code: String) extends SandboxedLanguage {
  val extension = "c"
  override val compileCommand = Some(Seq("gcc", "-Wall", filename))
  val command = Seq("./a.out")
}
