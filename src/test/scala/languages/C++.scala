package tests
import so.eval.{ EvaluationRequest, Router }
import so.eval.SandboxedLanguage.Result

import akka.pattern.ask

import scala.concurrent.Await
import scala.util.{ Failure, Try, Success }

class `C++` extends LanguageTest {

  describe("The C++ implementation") {
    it("should be able to successfully compile and run C++ code") {
      val evaluation = Router.route(
        "c++",
        EvaluationRequest(
          """#include <iostream>
            |using namespace std;
            |int main() {
            |  cout << "hello world!" << endl;
            |  cerr << "hi from stderr" << endl;
            |}""".stripMargin))

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

    it("should compile input files with .cxx and .cpp extensions") {
      val evaluation = Router.route(
        "c++",
        EvaluationRequest(
          """#include <iostream>
            |using namespace std;
            |int main() {
            |  cout << "hello world!" << endl;
            |  cerr << "hi from stderr" << endl;
            |}""".stripMargin,
          files = Some(
            Map(
              "foo.cpp" -> "bar",
              "baz.cxx" -> "buz"))))
      evaluation should not be (None)
      val compileCommand = evaluation.get.compileCommand.get.mkString(" ")
      compileCommand should include("foo.cpp")
      compileCommand should include("baz.cxx")
      evaluation.get.deleteHomeDirectory()
    }

    it("should be able to run with compilationOnly set") {
      val evaluation = Router.route(
        "c++",
        EvaluationRequest(
          """#include <iostream>
            |using namespace std;
            |int main() {
            |  cout << "hello world!" << endl;
            |  cerr << "hi from stderr" << endl;
            |}""".stripMargin,
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
