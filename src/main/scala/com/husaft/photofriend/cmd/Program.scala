package com.husaft.photofriend.cmd

import scopt.OptionParser

object Program {

  def main(args: Array[String]): Unit = {
    // Create parser
    val appName = "photofriend"
    val appVer = "1.x"
    val parser = new OptionParser[Config](appName) {
      head(appName, appVer)

      opt[Unit]("albums").action((_, c) =>
        c.copy(listAlbums = true)).text("list your albums")

      help("help").text("prints this usage text")
    }
    // Parse arguments
    val cfg = parser.parse(args, Config())
    if (cfg.isEmpty || args.isEmpty)
      parser.showUsage()
    else
      App.work(cfg.get)
  }
}