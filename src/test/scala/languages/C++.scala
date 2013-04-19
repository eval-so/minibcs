package tests
import so.eval.{EvaluationRequest, Router}
import so.eval.SandboxedLanguage.Result

import org.scalatest.{BeforeAndAfter, FunSpec, Inside, ParallelTestExecution}
import org.scalatest.matchers.ShouldMatchers

import scala.util.{Failure, Try, Success}

class `C++`
  extends FunSpec
  with ShouldMatchers
  with Inside
  with BeforeAndAfter
  with ParallelTestExecution {

  def setupSimpleCPPTest(code: String = """#include <iostream>
    using namespace std;
    int main() { cout << "hello" << endl; }""") = {
    val cppOption = Router.route("c++", code)
    cppOption should not be (None)
    val Some(cpp) = cppOption
    (cpp, cpp.evaluate)
  }

  describe("The C++ implementation") {
    it("should be able to successfully compile and run C++ code") {
      val (cpp, evaluated) = setupSimpleCPPTest()
      evaluated should be ('success)
      val Success(result) = evaluated
      inside(result) {
        case Result(stdout, stderr, wallTime, exitCode, compilationResult) =>
          stdout should be ("hello\n")
          // stderr should be ("")
          wallTime should be < 1000L
          exitCode should be (0)
          compilationResult should not be (None)
      }
    }

    it("should be able to handle input files") {
      val code = """int main() { system("ls"); }"""
      val eval = Router.route(
        "c++",
        EvaluationRequest(code, Some(Map("foo.cpp" -> "Zm9vYmFy"))))
        eval.get.compileCommand.get.mkString(" ") should include ("foo.cpp")
    }
  }
}
