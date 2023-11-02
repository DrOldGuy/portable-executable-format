// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

import java.nio.ByteBuffer;

/**
 * These are the values of the "magic number", a field in the optional header that determines if the
 * PE optional format is PE32 or PE32+ format.
 */
public enum MagicNumber {
  // @formatter:off
  PE_OPTIONAL_HEADER_UNKNOWN(0x0000),
  PE_OPTIONAL_HEADER_STD(0x010b),
  PE_OPTIONAL_HEADER_PLUS(0x020b);
  // @formatter:on

  private short value;

  private MagicNumber(int value) {
    byte[] bytes = {(byte)((value >> 8) & 0x000000ff), (byte)(value & 0x000000ff)};
    ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);

    buffer.put(bytes);
    buffer.rewind();
    this.value = buffer.getShort();
  }

  /**
   * Convert from a value to a MagicNumber.
   * 
   * @param value
   * @return
   */
  public static MagicNumber valueOf(int value) {
    short shValue = (short)value;

    for (MagicNumber mn : MagicNumber.values()) {
      if (shValue == mn.value) {
        return mn;
      }
    }

    return PE_OPTIONAL_HEADER_UNKNOWN;
  }
}
