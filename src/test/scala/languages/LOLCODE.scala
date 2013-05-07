package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class LOLCODE extends LanguageTest {

  describe("The LOLCODE implementation") {
    it("should be able to successfully evaluate LOLCODE") {
      val evaluation = Router.route(
        "lolcode",
        EvaluationRequest(
          """HAI 1.2
            |CAN HAS STDIO?
            |VISIBLE "hello world!"
            |KTHXBYE""".stripMargin))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("hello world!")
          wallTime should be < 2000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }
  }
}
