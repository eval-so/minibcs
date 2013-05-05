package tests
import so.eval.{ EvaluationRequest, Router }
import org.scalatest.{ BeforeAndAfter, FunSpec, ParallelTestExecution }
import org.scalatest.matchers.ShouldMatchers

class RouterSpec
  extends FunSpec
  with ShouldMatchers
  with BeforeAndAfter
  with ParallelTestExecution {

  describe("The Router") {
    it("should be able to route using the legacy route method") {
      val ruby = Router.route("ruby", "puts 1")
      ruby should not be (None)
      ruby.get.deleteHomeDirectory()

      Router.route("foobar", "foo bar") should be(None)
    }

    it("should be able to route using the new actor-style method") {
      val ruby = Router.route("ruby", EvaluationRequest("puts 1"))
      ruby should not be (None)
      ruby.get.deleteHomeDirectory()

      Router.route("foobar", EvaluationRequest("puts 1")) should be(None)
    }

    it("should be able to give a list evaluatable languages") {
      Router.languages.keys should contain("ruby")
    }
  }
}
