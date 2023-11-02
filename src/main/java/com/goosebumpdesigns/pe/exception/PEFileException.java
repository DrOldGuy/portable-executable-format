// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.exception;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This is a generic file exception that is thrown by an error in a seek or read operation on the PE
 * file.
 */
@SuppressWarnings("serial")
public class PEFileException extends PEException {

  /**
   * 
   */
  public PEFileException() {
    super();
  }

  /**
   * 
   * @param cause
   * @param path
   * @param offset
   * @param length
   */
  public PEFileException(IOException cause, Path path, long offset, int length) {
    super(String.format("Error reading %d bytes at offset %d in file %s.", length, offset, path),
        cause);
  }

  /**
   * @param message
   * @param cause
   */
  public PEFileException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public PEFileException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public PEFileException(Throwable cause) {
    super(cause);
  }
}
