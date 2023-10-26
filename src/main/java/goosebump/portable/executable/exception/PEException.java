// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.exception;

/**
 * This class is the base class of all DLL exceptions. 
 */
@SuppressWarnings("serial")
public class PEException extends RuntimeException {

  /**
   * 
   */
  public PEException() {}

  /**
   * @param message
   * @param cause
   */
  public PEException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public PEException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public PEException(Throwable cause) {
    super(cause);
  }

}
