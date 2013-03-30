package tests
import gd.eval.Router
import gd.eval.SandboxedLanguage.Result
import org.scalatest.{BeforeAndAfter, FunSpec, Inside, ParallelTestExecution}
import org.scalatest.matchers.ShouldMatchers

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
      evaluated should be ('right)
      val Right(result) = evaluated
      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult) =>
          stdout should be ("1")
          // stderr should be ("")
          wallTime should be < 1000L
          exitCode should be (0)
          compilationResult should be (None)
      }
    }

    it("should clean up after itself") {
      val (rb, evaluated) = setupSimpleRubyTest()
      rb.home.exists should be (false)
    }

    it("should time out after 5 seconds") {
      val (rb, evaluated) = setupSimpleRubyTest("puts 1; sleep 10")
      val Right(result) = evaluated
      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult) =>
          stdout should be ("1")
          // stderr should be ("")
          wallTime should be > 4900L
          wallTime should be < 5200L
          exitCode should be (124)
          compilationResult should be (None)
      }
    }
  }
}
