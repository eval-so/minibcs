/** (c) 2012 Ricky Elrod <ricky@elrod.me>
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *   http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing,
  * software distributed under the License is distributed on an
  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  * KIND, either express or implied.  See the License for the
  * specific language governing permissions and limitations
  * under the License.
  */

package org.breakpoint_eval.minibcs
import akka.actor._
import akka.pattern.ask
import akka.event.Logging
import language.postfixOps
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import scala.concurrent.util.duration._
import org.breakpoint_eval.common._

class BCS extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case BreakpointEvaluation.Request(
      language,
      code,
      arch,
      compilerFlags,
      runtimeFlags,
      stdin,
      simulate
    ) => {
      log.info("received a BE.Request")
      simulate match {
        case Some("timeout") => sender ! BreakpointEvaluation.Response(
          5.00,
          None,
          Map(
            'stdout -> "foobar",
            'stderr -> ""
          ),
          None,
          Some("A timeout occurred.")
        )
        case None => // Handle it like a production request.
          case _ => // Other cases.
      }
    }
    case _ => println("bar")
  }
}

object Daemon extends App {
  val system = ActorSystem("daemon")
  val myActor = system.actorOf(
    Props[BCS].withDispatcher("bcs-dispatcher"),
    name = "bcs01")

  val x = myActor.ask(BreakpointEvaluation.Request(
    "php",
    "<?php echo 'hi';",
    "x86_64",
    simulate = Some("timeout")))(5 seconds)

  x onSuccess {
    case _ => println("HAI")
  }
}
