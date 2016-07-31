package com.husaft.photofriend.cmd

import java.io.File

import scopt.OptionParser

object Program {

  def main(args: Array[String]): Unit = {
    // Create parser
    val appName = "photofriend"
    val appVer = "1.x"
    val parser = new OptionParser[Config](appName) {
      head(appName, appVer)

      opt[Unit]('a', "albums").action((_, c) =>
        c.copy(listAlbums = true)).text("list your albums")

      opt[Unit]('p', "photos").action((_, c) =>
        c.copy(listPhotos = true)).text("list your photos")

      opt[Unit]('u', "sync").action((_, c) =>
        c.copy(syncFolder = true)).text("sync your folder")

      opt[Long]('s', "set").action((v, c) =>
        c.copy(photoSetId = v)).text("the photo set's id")
        .valueName("<id>")

      opt[File]('f', "folder").action((v, c) =>
        c.copy(folder = v)).text("the local folder")
        .valueName("<path>")

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