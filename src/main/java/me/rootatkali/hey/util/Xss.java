package me.rootatkali.hey.util;

public class Xss {
  public static String deXss(String msg) {
    return msg.replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&apos;");
  }
}
