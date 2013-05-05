package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.actor.{ ActorSystem, Props }
import akka.pattern.ask
import akka.util.Timeout

import org.scalatest.{ BeforeAndAfter, FunSpec, Inside, ParallelTestExecution }
import org.scalatest.matchers.ShouldMatchers

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{ Failure, Try, Success }

class `C`
  extends FunSpec
  with ShouldMatchers
  with Inside
  with BeforeAndAfter
  with ParallelTestExecution {

  // Some really high timeout that we'll never hit unless something is really
  // really wrong.
  implicit val timeout = Timeout(20.seconds)
  val system = ActorSystem("Evaluate")
  val router = system.actorOf(Props(new Router))

  val code = """#include <stdio.h>
               |int main() {
               |  printf("hello world!\n");
               |  fprintf(stderr, "hi from stderr");
               |  return 0;
               |}""".stripMargin

  describe("The C implementation") {
    it("should be able to successfully compile and run C code") {
      val evaluation = Router.route("c", EvaluationRequest(code))

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

    it("should compile multiple input files with .c extensions") {
      val evaluation = Router.route(
        "c",
        EvaluationRequest(
          code,
          files = Some(
            Map(
              "foo.c" -> "bar",
              "baz.c" -> "buz"))))

      evaluation should not be (None)
      val compileCommand = evaluation.get.compileCommand.get.mkString(" ")
      compileCommand should include("foo.c")
      compileCommand should include("baz.c")
      evaluation.get.deleteHomeDirectory()
    }

    it("should be able to run with compilationOnly set") {
      val evaluation = Router.route(
        "c",
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
