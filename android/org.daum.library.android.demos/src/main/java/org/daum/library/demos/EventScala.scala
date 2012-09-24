package org.daum.library.demos

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 24/09/12
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
import scala.actors.Actor.actor
import scala.actors.Actor.loop
import scala.actors.Actor.react
import scala.concurrent.ops.spawn
object EventScala extends  App{

  case class Event(index: Int)

  test()
  def test() {
    val consumer = actor {
      var count = 0l
      val start = System.currentTimeMillis()
      loop {
        react {
          case Event(c) => count += 1
          case "End" =>
            val end = System.currentTimeMillis()
            println("Scala AVG:" + count * 1000.0 / (end - start))
            exit()
        }
      }
    }
    var running = true;
    for (i <- 0 to 1) {
      {
        spawn {
          while (running) {
            consumer ! Event(0)
          }
          consumer!"End"
        }
      }
    }
    Thread.sleep(5000)
    running = false
  }



}