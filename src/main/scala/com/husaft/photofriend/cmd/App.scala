package com.husaft.photofriend.cmd

import scala.collection.JavaConversions.collectionAsScalaIterable

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.typesafe.config.ConfigFactory

object App {

  def work(cfg: Config): Unit = {
    // Set options
    val conf = ConfigFactory.load();
    val prefix = "PhotoFriend."
    val key = conf.getString(prefix + "key")
    val secret = conf.getString(prefix + "secret")
    val user = conf.getString(prefix + "user")
    // Create API handle
    val api = new Flickr(key, secret, new REST());
    // Work on options
    if (cfg.listAlbums)
      listAlbums(api, user)
  }

  def listAlbums(api: Flickr, user: String) {
    // Get photo sets
    val psi = api.getPhotosetsInterface;
    val allAlbums = psi.getList(user);
    println(s"${allAlbums.getTotal} albums found.")
    // Get albums
    val albums = allAlbums.getPhotosets;
    for (album <- albums) {
      val title = album.getTitle
      val desc = album.getDescription
      val views = album.getViewCount
      println(s"${views}\t${title}\t'${desc}'")
    }
  }
}