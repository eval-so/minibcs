package so.eval

/** This encapsulates every input from the API.
  *
  * This is so that every SandboxedLanguage implementation can just accept an
  * [[EvaluationRequest]] and not have to be updated every time we want to send
  * new input variables to them.
  */
case class EvaluationRequest(
  code: String,
  files: Option[Map[String, String]] = None,
  compilationOnly: Boolean = false)
