// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.file;

import java.nio.ByteOrder;

/**
 * 
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
