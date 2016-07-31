package com.husaft.photofriend.cmd

case class Config(
  listAlbums: Boolean = false,
  listPhotos: Boolean = false,
  photoSetId: Long = -1L)
