// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import lombok.Getter;

/**
 * This class implements a buffer in which integers can be read in either byte order (big endian or
 * little endian).
 */
public class ByteOrderBuffer {
  private static final String UTC_TIME_ZONE = "UTC";

  @Getter
  private ByteOrder byteOrder;

  private byte[] buffer;
  
  @Getter
  private int length;

  /**
   * Create and initialize a new buffer object.
   * 
   * @param buffer The byte array buffer to use.
   * @param byteOrder The order of integers within the buffer.
   */
  public ByteOrderBuffer(byte[] buffer, ByteOrder byteOrder) {
    this.buffer = buffer;
    this.byteOrder = byteOrder;
    this.length = buffer.length;
  }

  /**
   * Read an unsigned short integer based on the byte order given when the byte order buffer was
   * created.
   * 
   * @param offset The offset to read.
   * @return The short value.
   */
  public short readShort(int offset) {
    ByteBuffer bytes = ByteBuffer.allocate(Short.BYTES);

    bytes.put(buffer, offset, Short.BYTES);
    bytes = bytes.order(byteOrder);
    bytes.rewind();

    return bytes.getShort();
  }

  public int readUnsignedShort(int offset) {
    short value = readShort(offset);

    int i1 = ((value >> 8) & (int)0xff) << 8;
    int i2 = value & (int)0xff;

    return i1 + i2;
  }

  /**
   * @param peHeaderTimestampOffset
   * @return
   */
  public int readInt(int offset) {
    ByteBuffer bytes = ByteBuffer.allocate(Integer.BYTES);

    bytes.put(buffer, offset, Integer.BYTES);
    bytes = bytes.order(byteOrder);
    bytes.rewind();

    return bytes.getInt();
  }

  /**
   * Read an unsigned int and return the value as a long (without the high-order bit set).
   * 
   * @param offset The offset to read.
   * @return The unsigned value as a long.
   */
  public long readUnsignedInt(int offset) {
    int value = readInt(offset);

    // value is unsigned, so copy the bits into a long.
    long lo1 = ((value >> 24) & (long)0xff) << 24;
    long lo2 = ((value >> 16) & (long)0xff) << 16;
    long lo3 = ((value >> 8) & (long)0xff) << 8;
    long lo4 = value & (long)0xff;

    return lo1 + lo2 + lo3 + lo4;
  }

  /**
   * @param offset
   * @return
   */
  public int readUnsignedByte(int offset) {
    return buffer[offset] & (int)0xff;
  }

  /**
   * Convert the long value to a big integer, treating it as an unsigned long.
   * 
   * @param offset
   * @return
   */
  public BigInteger readUnsignedLong(int offset) {
    ByteBuffer bytes = ByteBuffer.allocate(Long.BYTES);

    bytes.put(buffer, offset, Long.BYTES);
    bytes.order(byteOrder);
    bytes.rewind();

    long value = bytes.getLong();

    if(value > -0L) {
      return BigInteger.valueOf(value);
    }

    int upper = (int)(value >>> 32);
    int lower = (int)value;

    return (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32)
        .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
  }

  /**
   * Returns a subset of the buffer at the given offset and size.
   * 
   * @param offset
   * @param size
   * @return
   */
  public byte[] bytes(int offset, int size) {
    return Arrays.copyOfRange(buffer, offset, offset + size);
  }

  /**
   * 
   */
  @Override
  public String toString() {
    return "ByteOrderBuffer [byteOrder=" + byteOrder + ", buffer=" + Arrays.toString(buffer) + "]";
  }

  /**
   * @param offset
   * @return
   */
  public LocalDateTime readTimestamp(int offset) {
    long seconds = readUnsignedInt(offset);
    long millis = seconds * 1000;

    return Instant.ofEpochMilli(millis).atZone(ZoneId.of(UTC_TIME_ZONE)).toLocalDateTime();
  }

  /**
   * @return
   */
  public byte[] bytes() {
    return Arrays.copyOf(buffer, buffer.length);
  }
}
