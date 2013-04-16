package tests
import so.eval.{EvaluationRequest, Router}
import org.scalatest.{BeforeAndAfter, FunSpec, ParallelTestExecution}
import org.scalatest.matchers.ShouldMatchers

class RouterSpec
  extends FunSpec
  with ShouldMatchers
  with BeforeAndAfter
  with ParallelTestExecution {

  describe("The Router") {
    it("should be able to route using the legacy route method") {
      Router.route("ruby", "puts 1") should not be (None)
      Router.route("foobar", "foo bar") should be (None)
    }

    it("should be able to route using the new actor-style method") {
      Router.route("ruby", EvaluationRequest("puts 1")) should not be (None)
      Router.route("foobar", EvaluationRequest("puts 1")) should be (None)
    }

    it("should be able to give a list evaluatable languages") {
      Router.languages.keys should contain ("ruby")
    }
  }
}
