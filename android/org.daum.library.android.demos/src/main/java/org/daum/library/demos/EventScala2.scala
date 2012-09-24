package org.daum.library.demos

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 24/09/12
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */

import scala.concurrent.ops.spawn
import akka.actor.{Props, ActorSystem, Actor}


object EventScala2 extends App {

  case class Event(index: Int)

  test()

  def test() {
    class MyActor extends Actor {

      def receive = {
        case Event(c) => {
          //Thread.sleep(1)
          count += 1
        }
        case "End" =>
          val end = System.currentTimeMillis()
          println("Scala AVG:" + count * 1000.0 / (end - start))
          exit()
      }

      var count = 0l
      val start = System.currentTimeMillis()
    }

    val system = ActorSystem("HelloSystem")
    // default Actor constructor
    val helloActor = system.actorOf(Props[MyActor], name = "helloactor")

    var running = true;
    for (i <- 0 to 1) {
      {
        spawn {
          while (running) {
            helloActor ! Event(0)
          }
          helloActor ! "End"
        }
      }
    }
    Thread.sleep(10000)
    running = false
  }


}