// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import java.nio.ByteBuffer;

/**
 * 
 */
public enum PEMagicNumber {
  // @formatter:off
  PE_OPTIONAL_HEADER_UNKNOWN(0x0000),
  PE_OPTIONAL_HEADER_STD(0x010b),
  PE_OPTIONAL_HEADER_PLUS(0x020b);
  // @formatter:on

  private short value;

  private PEMagicNumber(int value) {
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
  public static PEMagicNumber valueOf(int value) {
    short shValue = (short)value;

    for (PEMagicNumber mn : PEMagicNumber.values()) {
      if (shValue == mn.value) {
        return mn;
      }
    }

    return PE_OPTIONAL_HEADER_UNKNOWN;
  }
}
