// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.exception;

/**
 * This is thrown if the PE signature is not found at the expected location. This indicates that
 * either the file is not a PE file, or is not in a format recognized by this project.
 */
@SuppressWarnings("serial")
public class PESignatureException extends PEException {

  /**
   * @param message
   */
  public PESignatureException(String message) {
    super(message);
  }
}
