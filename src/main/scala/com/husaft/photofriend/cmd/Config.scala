package com.husaft.photofriend.cmd

import java.io.File

case class Config(
  listAlbums: Boolean = false,
  listPhotos: Boolean = false,
  syncFolder: Boolean = false,
  photoSetId: Long = -1L,
  folder: File = new File("."),
  noUpload: Boolean = false)
