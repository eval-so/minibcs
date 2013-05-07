package tests

import so.eval.Router

import akka.actor.{ ActorSystem, Props }
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

import org.scalatest.{ BeforeAndAfter, FunSpec, Inside, ParallelTestExecution }
import org.scalatest.matchers.ShouldMatchers

/** A common trait that all of our language tests can extend. */
trait LanguageTest
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

}
