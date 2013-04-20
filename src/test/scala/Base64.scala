package tests
import so.eval.{EvaluationRequest, Router}

import org.scalatest.{BeforeAndAfter, FunSpec, Inside, ParallelTestExecution}
import org.scalatest.matchers.ShouldMatchers

class Base64
  extends FunSpec
  with ShouldMatchers
  with Inside
  with BeforeAndAfter
  with ParallelTestExecution {

  describe("Base 64 handling") {
    it("should encode output files correctly") {
      val res = Router.route(
        "ruby",
        "`echo 'foobar' > output/foo`; puts 123").get.evaluate
      res.get.outputFiles.get.head._2 should be ("Zm9vYmFyCg==")
    }

    it("should decode input files correctly") {
      val req = EvaluationRequest(
        "puts File.read('foo')",
        files = Some(Map("foo" -> "Zm9vYmFyCg==")))
      val res = Router.route("ruby", req).get.evaluate
      res.get.stdout.trim should be ("foobar")
    }
  }
}
