// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import java.nio.ByteOrder;

/**
 * This class contains data for a data section header. It is simply a {@link ByteOrderBuffer} with a
 * different name to distinguish its usage.
 */
public class SectionBuffer extends ByteOrderBuffer {

  /**
   * @param buffer
   * @param byteOrder
   */
  public SectionBuffer(byte[] buffer, ByteOrder byteOrder) {
    super(buffer, byteOrder);
  }

}
