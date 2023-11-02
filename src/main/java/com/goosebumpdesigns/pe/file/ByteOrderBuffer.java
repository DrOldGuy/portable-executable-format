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
 * This class implements a buffer in which integers can be read in either byte order (big-endian or
 * little-endian). PE files are written by C/C++, which stores numeric values in little-endian
 * order. Java is big-endian. This class converts between the two methods.
 * 
 * I could have used a {@link ByteBuffer} to handle these operations but ByteBuffer isn't a simple
 * class. There is a lot of support for mapping physical file buffers to memory buffers, which is a
 * little beyond the scope of requirements. It's way overkill, so I wrote my own.
 * 
 * This class supports retrieving longs, ints, shorts, and bytes in a signed and unsigned manner in
 * either big-endian or little-endian ordered buffers.
 * 
 * This class also supports timestamps as the number of seconds since 1-Jan-1970 stored as an
 * unsigned int value.
 * 
 * Because Java does not support unsigned values, this class returns unsigned values in the next
 * largest integer type. So, unsigned bytes are returned as shorts, unsigned shorts are returned as
 * ints, etc.
 */
public class ByteOrderBuffer {
  private static final String UTC_TIME_ZONE = "UTC";

  /**
   * We can't easily extend ByteBuffer as it's an abstract class with a BUNCH of abstract methods. A
   * class we *could* use is java.nio.HeapByteBufferR, but it's a package-access class and it's too
   * complex to reliably copy to another package.
   */
  private byte[] buffer;

  @Getter
  private ByteOrder byteOrder;

  /**
   * Create and initialize a new buffer object.
   * 
   * @param buffer The byte array buffer to use.
   * @param byteOrder The order of integers within the buffer.
   */
  public ByteOrderBuffer(byte[] buffer, ByteOrder byteOrder) {
    this.buffer = buffer;
    this.byteOrder = byteOrder;
  }

  /**
   * Returns the length of the internal buffer.
   * 
   * @return The length of the buffer.
   */
  public int size() {
    return buffer.length;
  }

  /**
   * Returns a String representation of this object.
   */
  @Override
  public String toString() {

    return String.format("%s [order=%s, length=%d, buffer=%s]", getClass().getSimpleName(),
        getByteOrder(), size(), Arrays.toString(getBytes()));
  }

  /**
   * Returns a signed 32-bit integer value.
   * 
   * @param offset The offset of the int.
   * @return The signed int.
   */
  public int getInt(int offset) {
    byte[] buf = Arrays.copyOfRange(buffer, offset, offset + Integer.BYTES);
    int result = 0;

    for(int i = 0; i < Integer.BYTES; i++) {
      int pos = getByteOrder() == ByteOrder.BIG_ENDIAN ? i : Integer.BYTES - i - 1;
      result = (int)((result << 8) + (buf[pos] & 0xff));
    }

    return result;
  }

  /**
   * Returns a signed 16-bit short value.
   * 
   * @param offset The offset of the short.
   * @return The signed short.
   */
  public short getShort(int offset) {
    byte[] buf = Arrays.copyOfRange(buffer, offset, offset + Short.BYTES);
    short result = 0;

    for(int i = 0; i < Short.BYTES; i++) {
      int pos = getByteOrder() == ByteOrder.BIG_ENDIAN ? i : Short.BYTES - i - 1;
      result = (short)((result << 8) + (short)(buf[pos] & 0xff));
    }

    return result;
  }

  /**
   * Returns a signed 8-bit byte value.
   * 
   * @param offset The offset of the byte.
   * @return The signed byte.
   */
  public byte getByte(int offset) {
    return buffer[offset];
  }

  /**
   * Returns an unsigned 8-bit value as a short.
   * 
   * @param offset The offset of the unsigned byte.
   * @return The unsigned byte value as a short.
   */
  public short getUnsignedByte(int offset) {
    return (short)(getByte(offset) & 0xff);
  }

  /**
   * Returns an unsigned 32-bit value as a long (64-bit) value.
   * 
   * @param offset The offset of the unsigned int.
   * @return The unsigned int value as a long.
   */
  public long getUnsignedInt(int offset) {
    int value = getInt(offset);

    if(value >= 0) {
      return value;
    }

    // value is unsigned, so copy the bits into a long.
    long lo1 = ((value >>> 24) & (long)0xff) << 24;
    long lo2 = ((value >>> 16) & (long)0xff) << 16;
    long lo3 = ((value >>> 8) & (long)0xff) << 8;
    long lo4 = value & (long)0xff;

    return lo1 + lo2 + lo3 + lo4;
  }

  /**
   * Returns an unsigned 16-bit value as an int (32-bit) value.
   * 
   * @param offset The offset of the unsigned short.
   * @return The unsigned short value as an int.
   */
  public int getUnsignedShort(int offset) {
    short value = getShort(offset);

    if(value >= 0) {
      return value;
    }

    int i1 = ((value >> 8) & (int)0xff) << 8;
    int i2 = value & (int)0xff;

    return i1 + i2;
  }

  /**
   * Returns an unsigned 64-bit value as a BigInteger.
   * 
   * @param offset The offset of the unsigned long.
   * @return The unsigned long value as a BigInteger.
   */
  public BigInteger getUnsignedLong(int offset) {
    long value = getLong(offset);

    if(value >= 0) {
      return BigInteger.valueOf(value);
    }

    int upper = (int)(value >>> 32);
    int lower = (int)value;

    return (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32)
        .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
  }

  /**
   * Returns a signed 64-bit value as a long.
   * 
   * @param offset The offset of the signed long.
   * @return The signed long value.
   */
  public long getLong(int offset) {
    int size = Long.BYTES;
    byte[] buf = Arrays.copyOfRange(buffer, offset, offset + size);
    long result = 0;

    for(int i = 0; i < size; i++) {
      int pos = getByteOrder() == ByteOrder.BIG_ENDIAN ? i : size - i - 1;
      result = (long)((result << 8) + (buf[pos] & 0xff));
    }

    return result;
  }

  /**
   * Returns a subset of the buffer at the given offset and size.
   * 
   * @param offset The start of the buffer to return.
   * @param size The size of the returned buffer.
   * @return The buffer.
   */
  public byte[] getBytes(int offset, int size) {
    return Arrays.copyOfRange(buffer, offset, offset + size);
  }

  /**
   * Convert the unsigned 32-bit value at the given offset to a {@link LocalDateTime}. The value
   * must be the number of seconds since 01-Jan-1970. There is no time zone information in the value
   * so interpret the timestamp as UTC.
   * 
   * @param offset The position in the buffer.
   * @return A timestamp value.
   */
  public LocalDateTime getTimestamp(int offset) {
    long seconds = getUnsignedInt(offset);
    long millis = seconds * 1000;

    return Instant.ofEpochMilli(millis).atZone(ZoneId.of(UTC_TIME_ZONE)).toLocalDateTime();
  }

  /**
   * @return A copy of the entire internal buffer.
   */
  public byte[] getBytes() {
    return getBytes(0, size());
  }

}
