// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.optionalheader;

import com.goosebumpdesigns.pe.PEOptionalHeader;
import com.goosebumpdesigns.pe.exception.PESignatureException;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.model.type.MagicNumber;

/**
 * This class reads the magic number field in the optional header and returns either a
 * {@link OptionalHeaderStd} object or a {@link OptionalHeaderPlus} object.
 */
public abstract class OptionalHeaderFactory {
  private static final int MAGIC_NUMBER_OFFSET = 0;

  /**
   * Returns either a {@link OptionalHeaderStd} or {@link OptionalHeaderPlus} class object based on
   * the value of the magic number field in the optional header.
   * 
   * @param buffer The optional header buffer.
   * @return The correct header class.
   */
  public static PEOptionalHeader createOptionalHeader(ByteOrderBuffer buffer) {
    int value = buffer.getUnsignedShort(MAGIC_NUMBER_OFFSET);

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
