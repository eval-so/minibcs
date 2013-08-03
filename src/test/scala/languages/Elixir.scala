package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class Elixir extends LanguageTest {

  val code = """IO.puts "hello world!"
               |IO.puts :stderr, "hello from stderr"""".stripMargin

  describe("The Elixir implementation") {
    it("should be able to successfully compile and evaluate Elixir") {
      val evaluation = Router.route(
        "elixir",
        EvaluationRequest(code))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("hello world!")
          stderr.trim should be("hello from stderr")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should be (None)
      }
    }
  }
}
