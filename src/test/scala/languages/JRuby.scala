package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class JRuby extends LanguageTest {

  describe("The JRuby implementation") {
    it("should be able to successfully evaluate JRuby in 1.8 mode") {
      val evaluation = Router.route(
        "jruby18",
        EvaluationRequest("""puts RUBY_VERSION"""))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should include("1.8")
          wallTime should be < 5000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }

    it("should be able to successfully evaluate JRuby in 1.9 mode") {
      val evaluation = Router.route(
        "jruby19",
        EvaluationRequest("""puts RUBY_VERSION"""))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should include("1.9")
          wallTime should be < 5000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }

  }
}
