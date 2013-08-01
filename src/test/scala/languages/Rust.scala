package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class Rust extends LanguageTest {

  val code = """use std::io;
               |fn main() {
               |  println("hello world!");
               |  io::stderr().write_line("hi from stderr");
               |}""".stripMargin

  describe("The Rust implementation") {
    it("should be able to successfully compile and run Rust code") {
      val evaluation = Router.route("rust", EvaluationRequest(code))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("hello world!")
          stderr.trim should be("hi from stderr")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should not be (None)
      }
    }

    it("should be able to run with compilationOnly set") {
      val evaluation = Router.route(
        "rust",
        EvaluationRequest(
          code,
          compilationOnly = true))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be("")
          stderr.trim should be("")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }
  }
}
