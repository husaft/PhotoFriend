package com.husaft.photofriend.cmd

import java.io.File

import scala.collection.JavaConversions.collectionAsScalaIterable

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.typesafe.config.ConfigFactory
import com.flickr4java.flickr.uploader.Uploader
import com.flickr4java.flickr.uploader.UploadMetaData

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
    // Authenticate yourself
    val auth = AuthIt.doAuth(api)
    api.setAuth(auth)
    // Work on options
    if (cfg.listAlbums)
      listAlbums(api, user)
    else if (cfg.listPhotos)
      listPhotos(api, cfg.photoSetId)
    else if (cfg.syncFolder)
      syncFolder(api, cfg.folder, cfg.photoSetId)
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

  def syncFolder(api: Flickr, folder: File, setId: Long) {
    val picExts = List("png", "jpg")
    val files = Helper.getListOfFiles(folder, picExts)
    val psi = api.getPhotosetsInterface;
    val photos = psi.getPhotos(setId + "", 1024, 1);
    val pmap = photos.map { p => (p.getTitle, p) }.toMap
    val upl = api.getUploader
    for (file <- files) {
      var name = file.getName
      picExts.foreach { e => name = name.replace("." + e, "") }
      val exists = pmap.contains(name)
      if (!exists)
        upload(upl, file, setId)
    }
  }

  def upload(api: Uploader, file: File, setId: Long) {
    println(s"Uploading '${file}' to ${setId}...")
    val parts = file.getName.split("\\.")
    val name = parts(0)
    val ext = parts(1)
    val mime = "image/" + ext
    val data = new UploadMetaData()
    data.setContentType(mime)
    data.setDescription(null)
    data.setFilemimetype(mime)
    data.setFilename(name)
    data.setHidden(false)
    data.setPublicFlag(true)
    data.setTitle(name)
    val pid = api.upload(file, data)
    println(s" --> got photo id ${pid}!")
  }
}