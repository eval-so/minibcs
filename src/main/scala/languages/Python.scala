package so.eval.languages
import so.eval.{ EvaluationRequest, SandboxedLanguage }

trait Python extends SandboxedLanguage {
  val extension = "py"
}

case class Python2(evaluation: EvaluationRequest) extends Python {
  val command = Seq("python2", filename)
}

case class Python3(evaluation: EvaluationRequest) extends Python {
  val command = Seq("python3", filename)
}
