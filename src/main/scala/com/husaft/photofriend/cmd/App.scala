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
    else if (cfg.listPhotos)
      listPhotos(api, cfg.photoSetId)
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
      val id = album.getId
      println(s"${id}\t${title}\t${desc}\t${views}")
    }
  }

  def listPhotos(api: Flickr, setId: Long) {
    // Get photos
    val psi = api.getPhotosetsInterface;
    val allPhotos = psi.getPhotos(setId + "", 1024, 1);
    println(s"${allPhotos.getTotal} photos found.")
    // Loop over them
    for (photo <- allPhotos) {
      val id = photo.getId
      val desc = photo.getDescription
      val title = photo.getTitle
      println(s"${id}\t${title}\t${desc}")
    }
  }
}