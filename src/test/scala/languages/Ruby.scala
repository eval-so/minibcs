package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import org.scalatest.{ BeforeAndAfter, FunSpec, Inside, ParallelTestExecution }
import org.scalatest.matchers.ShouldMatchers

import scala.util.{ Failure, Try, Success }

class Ruby
  extends FunSpec
  with ShouldMatchers
  with Inside
  with BeforeAndAfter
  with ParallelTestExecution {

  def setupSimpleRubyTest(code: String = "puts 1") = {
    val rbOption = Router.route("ruby", code)
    rbOption should not be (None)
    val Some(rb) = rbOption
    (rb, rb.evaluate)
  }

  describe("The Ruby implementation") {
    it("should be able to successfully evaluate Ruby") {
      val (rb, evaluated) = setupSimpleRubyTest()
      evaluated should be('success)
      val Success(result) = evaluated
      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout should be("1\n")
          // stderr should be ("")
          wallTime should be < 1000L
          exitCode should be(0)
          compilationResult should be(None)
      }
    }

    it("should clean up after itself") {
      val (rb, evaluated) = setupSimpleRubyTest()
      rb.home.exists should be(false)
    }

    it("should time out after 5 seconds") {
      val (rb, evaluated) = setupSimpleRubyTest("puts 1; sleep 10")
      val Success(result) = evaluated
      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult, outputFiles) =>
          stdout should be("1\n")
          // stderr should be ("")
          wallTime should be > 4900L
          wallTime should be < 5500L
          exitCode should be(124)
          compilationResult should be(None)
      }
    }

    it("should be able to handle input files") {
      val code = """puts File.read("foo.txt")"""
      val eval = Router.route(
        "ruby",
        EvaluationRequest(
          code,
          files = Some(Map("foo.txt" -> "Zm9vYmFy"))))
      val result = eval.get.evaluate
      result.get.stdout should be("foobar\n")
    }

    it("should be able to handle stdin") {
      val code = """puts STDIN.gets"""
      val eval = Router.route(
        "ruby",
        EvaluationRequest(
          code,
          stdin = Some("testing")))
      val result = eval.get.evaluate
      result.get.stdout should be("testing\n")
    }
  }
}
