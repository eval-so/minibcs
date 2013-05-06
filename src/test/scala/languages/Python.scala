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

class Python
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
