package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class Scala extends LanguageTest {

  describe("The Scala implementation") {
    it("should be able to successfully evaluate Scala") {
      val evaluation = Router.route(
        "scala",
        EvaluationRequest(
          """object EvalSO extends App {
            |  println("hello world!")
            |}""".stripMargin))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("hello world!")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should not be (None)
      }
    }
  }
}
