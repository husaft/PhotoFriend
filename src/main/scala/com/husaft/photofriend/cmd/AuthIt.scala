package com.husaft.photofriend.cmd

import java.util.Scanner
import com.flickr4java.flickr._
import com.flickr4java.flickr.auth._
import org.scribe.model._
import java.awt.Desktop
import java.net.URI
import com.typesafe.config.ConfigFactory

object AuthIt {

  def doAuth(api: Flickr): Auth = {
    val authIntf = api.getAuthInterface

    val prefix = "Auth.";
    val conf = ConfigFactory.load();
    val token = conf.getString(prefix + "token")
    val secret = conf.getString(prefix + "secret");

    var requestToken: Token = null

    if (token.isEmpty()) {
      val scanner = new Scanner(System.in)
      val token = authIntf.getRequestToken()
      val url = authIntf.getAuthorizationUrl(token, Permission.DELETE)
      Desktop.getDesktop.browse(URI.create(url))
      println("Paste in the token it gives you:")
      print(">>")
      val tokenKey = scanner.nextLine()
      scanner.close()
      requestToken = authIntf.getAccessToken(token, new Verifier(tokenKey))
      println("Authentication success")
    } else {
      requestToken = new Token(token, secret)
    }

    val auth = authIntf.checkToken(requestToken)
    println(" Token: " + requestToken.getToken());
    println(" Secret: " + requestToken.getSecret());
    println(" Id: " + auth.getUser().getId())
    println(" Realname: " + auth.getUser().getRealName())
    println(" Username: " + auth.getUser().getUsername())
    println(" Permission: " + auth.getPermission().getType())

    auth
  }
}