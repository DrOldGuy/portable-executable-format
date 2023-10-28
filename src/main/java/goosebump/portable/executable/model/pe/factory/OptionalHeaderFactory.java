// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe.factory;

import goosebump.portable.executable.exception.PESignatureException;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.model.MagicNumber;
import goosebump.portable.executable.model.pe.PEOptionalHeader;
import goosebump.portable.executable.model.pe.PEOptionalHeaderPlus;
import goosebump.portable.executable.model.pe.PEOptionalHeaderStd;

/**
 * 
 */
public abstract class OptionalHeaderFactory {

  /**
   * @param buffer
   * @return
   */
  public static PEOptionalHeader createOptionalHeader(ByteOrderBuffer buffer) {
    int value = buffer.readUnsignedShort(0);
    
    switch(MagicNumber.valueOf(value)) {
      case PE_OPTIONAL_HEADER_PLUS:
        return new PEOptionalHeaderPlus(buffer);
        
      case PE_OPTIONAL_HEADER_STD:
        return new PEOptionalHeaderStd(buffer);
      
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
