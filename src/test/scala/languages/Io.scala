package tests
import so.eval.{EvaluationRequest, Router}
import so.eval.SandboxedLanguage.Result

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import org.scalatest.{BeforeAndAfter, FunSpec, Inside, ParallelTestExecution}
import org.scalatest.matchers.ShouldMatchers

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Try, Success}

class Io
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


  describe("The Io implementation") {
    it("should be able to successfully evaluate Io") {
      val evaluation = Router.route(
        "io",
        EvaluationRequest(""""hello world!" println"""))

      evaluation should not be (None)

      val future = router ? evaluation.get
      val futureResult = Await.result(future, timeout.duration).asInstanceOf[Try[Result]]

      futureResult should be ('success)
      val Success(result) = futureResult

      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout.trim should be ("hello world!")
          wallTime should be < 1000L
          exitCode should be (0)
          compilationResult should be (None)
      }
    }
  }
}
