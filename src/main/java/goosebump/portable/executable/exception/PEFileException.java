// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.exception;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 */
@SuppressWarnings("serial")
public class PEFileException extends PEException {

  /**
   * 
   */
  public PEFileException() {
    super();
  }
  
  public PEFileException(IOException e, Path path, long offset, int length) {
    super(String.format("Error reading %d bytes at offset %d in file %s.",
        length, offset, path), e);
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
