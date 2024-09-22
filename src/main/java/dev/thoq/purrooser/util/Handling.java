package dev.thoq.purrooser.util;

import static java.lang.System.err;

public class Handling {
  public static void resourceNotFound(String resource) {
    err.println("Resource not found: " + resource);
  }
}
