package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class Python extends LanguageTest {

  describe("The Python implementation") {
    it("should be able to successfully evaluate Python 2") {
      val evaluation = Router.route(
        "python2",
        EvaluationRequest(
          """import sys
            |print sys.version_info.major""".stripMargin))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("2")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }

    it("should be able to successfully evaluate Python") {
      val evaluation = Router.route(
        "python3",
        EvaluationRequest(
          """import sys
            |print(sys.version_info.major)""".stripMargin))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("3")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }
  }
}
