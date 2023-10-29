// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.optionalheader;

import com.goosebumpdesigns.pe.PEOptionalHeader;
import com.goosebumpdesigns.pe.exception.PESignatureException;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.model.type.MagicNumber;

/**
 * 
 */
public abstract class OptionalHeaderFactory {
  private static final int MAGIC_NUMBER_OFFSET = 0;

  /**
   * @param buffer
   * @return
   */
  public static PEOptionalHeader createOptionalHeader(ByteOrderBuffer buffer) {
    int value = buffer.readUnsignedShort(MAGIC_NUMBER_OFFSET);
    
    switch(MagicNumber.valueOf(value)) {
      case PE_OPTIONAL_HEADER_PLUS:
        return new OptionalHeaderPlus(buffer);
        
      case PE_OPTIONAL_HEADER_STD:
        return new OptionalHeaderStd(buffer);
      
      default:
        throw new PESignatureException(buildMessage(value));
      
    }
  }

  /**
   * @param value
   * @return
   */
  private static String buildMessage(int value) {
    return String.format("Magic number 0x%04x is not correct.", value);
  }

}
