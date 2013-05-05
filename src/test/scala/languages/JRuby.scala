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

class JRuby
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
