package org.daum.library.javase.kestrelChannels

import com.twitter.ostrich.admin.RuntimeEnvironment
import net.lag.kestrel.Kestrel
import com.twitter.conversions.storage._
import net.lag.kestrel.config._
import com.twitter.logging.Logger
import com.twitter.logging.config._
import java.lang.Thread
/**
 * Created by IntelliJ IDEA.
 * User: jedartois@gmail.com
 * Date: 29/11/11
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
class KestrelServer(host : String,port :Int,queuePath : String ,filepathlog :String,persitentQueue : Boolean) {

  private val PORT = this.port
  private var kestrel: Kestrel = null
  private val log = Logger.get(getClass)


  val runtime = RuntimeEnvironment(this, Array())
  Kestrel.runtime = runtime

  runServer()


  def runServer() = {
    new Thread(){
      override  def run() {
        val defaultConfig = new QueueBuilder() {
          maxSize = 128.megabytes
          if(persitentQueue)
          {
            keepJournal = true
            maxJournalSize = 512.megabytes
          }else {
            keepJournal = false
            maxMemorySize = 512.megabytes
          }
        }.apply()

        // make a queue specify max_items and max_age
        val UpdatesConfig = new QueueBuilder() {
          maxSize = 128.megabytes
          if(persitentQueue)
          {
            keepJournal = true
            maxJournalSize = 512.megabytes
          }else {
            keepJournal = false
            maxMemorySize = 512.megabytes
          }
        }

        //""
        kestrel = new Kestrel(defaultConfig, List(UpdatesConfig),host,
          Some(PORT), None,queuePath, Protocol.Ascii, None, None, 1)

        kestrel.start()
      }
    }.start()
  }

  def stopServer()= {
    kestrel.shutdown()

  }

  def reloadServer() = {
    //todo change conf
    kestrel.reload()
  }

  val   config = new LoggerConfig {
    level = Level.DEBUG
    handlers = new FileHandlerConfig {
      filename = filepathlog
      roll = Policy.Never
    }
  }
  //config()

}