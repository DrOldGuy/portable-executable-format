// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the methods of {@link ByteOrderBuffer}, which is used to convert from a file
 * buffer to various data types based on the endian type (big-endian or little-endian) of the file
 * data.
 */
class ByteOrderBufferTest extends BufferSupport {
  private ByteOrderBuffer bigEndianBuffer;
  private ByteOrderBuffer littleEndianBuffer;
  private byte[] bigEndianBytes;
  private byte[] littleEndianBytes;

  /**
   * Initialize the instance variables before each test.
   */
  @BeforeEach
  void init() {
    bigEndianBytes = initBuffer(ByteOrder.BIG_ENDIAN);
    bigEndianBuffer = new ByteOrderBuffer(bigEndianBytes, ByteOrder.BIG_ENDIAN);

    littleEndianBytes = initBuffer(ByteOrder.LITTLE_ENDIAN);
    littleEndianBuffer = new ByteOrderBuffer(littleEndianBytes, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * Test method for {@link ByteOrderBuffer#size()}.
   */
  @Test
  void testSize() {
    // Given: a byte order buffer
    ByteOrderBuffer buf = bigEndianBuffer;
    int expected = size;

    // When: the size is obtained
    int actual = buf.size();

    // Then: the size is what is expected
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getInt(int)}.
   */
  @Test
  void testGetInt() {
    testGetInt(ByteOrder.BIG_ENDIAN);
    testGetInt(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetInt(ByteOrder order) {
    // Given: a big endian buffer with an int
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    int expected = anInt;

    // When: an integer is retrieved
    int actual = buf.getInt(intPos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getLong(int)}.
   */
  @Test
  void testGetLong() {
    testGetLong(ByteOrder.BIG_ENDIAN);
    testGetLong(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetLong(ByteOrder order) {
    // Given: a big endian buffer with a long
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    long expected = aLong;

    // When: an integer is retrieved
    long actual = buf.getLong(longPos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getShort(int)}.
   */
  @Test
  void testGetShort() {
    testGetShort(ByteOrder.BIG_ENDIAN);
    testGetShort(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetShort(ByteOrder order) {
    // Given: a big endian buffer with an int
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    short expected = aShort;

    // When: an integer is retrieved
    short actual = buf.getShort(shortPos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getByte(int)}.
   */
  @Test
  void testGetByte() {
    testGetByte(ByteOrder.BIG_ENDIAN);
    testGetByte(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetByte(ByteOrder order) {
    // Given: a big endian buffer with a byte
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    byte expected = aByte;

    // When: an integer is retrieved
    byte actual = buf.getByte(bytePos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getUnsignedInt(int)}.
   */
  @Test
  void testGetUnsignedInt() {
    testGetUnsignedInt(anInt, intPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedInt(anInt, intPos, ByteOrder.LITTLE_ENDIAN);
    testGetUnsignedInt(negInt, negIntPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedInt(negInt, negIntPos, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * 
   * @param anInt
   * @param intPos
   * @param order
   */
  private void testGetUnsignedInt(int anInt, int intPos, ByteOrder order) {
    // Given: a big endian buffer with an int
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    long l0 = ((long)anInt >>> 24 & 0x000000ff) << 24;
    long l1 = ((long)anInt >>> 16 & 0x000000ff) << 16;
    long l2 = ((long)anInt >>> 8 & 0x000000ff) << 8;
    long l3 = (long)anInt & 0x000000ff;
    long expected = l0 + l1 + l2 + l3;

    // When: an integer is retrieved
    long actual = buf.getUnsignedInt(intPos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getUnsignedLong(int)}.
   */
  @Test
  void testGetUnsignedLong() {
    testGetUnsignedLong(aLong, longPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedLong(aLong, longPos, ByteOrder.LITTLE_ENDIAN);
    testGetUnsignedLong(negLong, negLongPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedLong(negLong, negLongPos, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * 
   * @param value
   * @param pos
   * @param order
   */
  private void testGetUnsignedLong(long value, int pos, ByteOrder order) {
    // Given: a big endian buffer with a long
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    int upper = (int)(value >>> 32);
    int lower = (int)value;

    BigInteger expected = (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32)
        .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));

    // When: a long is retrieved
    BigInteger actual = buf.getUnsignedLong(pos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getUnsignedShort(int)}.
   */
  @Test
  void testGetUnsignedShort() {
    testGetUnsignedShort(aShort, shortPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedShort(aShort, shortPos, ByteOrder.LITTLE_ENDIAN);
    testGetUnsignedShort(negShort, negShortPos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedShort(negShort, negShortPos, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * 
   * @param aShort
   * @param shortPos
   * @param order
   */
  private void testGetUnsignedShort(short aShort, int shortPos, ByteOrder order) {
    // Given: a big endian buffer with an int
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    int i0 = ((int)aShort >>> 8 & 0x000000ff) << 8;
    int i1 = (int)aShort & 0x000000ff;
    int expected = i0 + i1;

    // When: an integer is retrieved
    int actual = buf.getUnsignedShort(shortPos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getUnsignedByte(int)}.
   */
  @Test
  void testGetUnsignedByte() {
    testGetUnsignedByte(aByte, bytePos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedByte(aByte, bytePos, ByteOrder.LITTLE_ENDIAN);
    testGetUnsignedByte(negByte, negBytePos, ByteOrder.BIG_ENDIAN);
    testGetUnsignedByte(negByte, negBytePos, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * 
   * @param aByte
   * @param bytePos
   * @param order
   */
  private void testGetUnsignedByte(byte aByte, int bytePos, ByteOrder order) {
    // Given: a big endian buffer with a byte
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    short expected = (short)(aByte & 0x00ff);

    // When: an integer is retrieved
    short actual = buf.getUnsignedByte(bytePos);

    // Then: the integer value returned is correct.
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getBytes(int, int)}.
   */
  @Test
  void testGetBytesArray() {
    testGetBytesArray(ByteOrder.BIG_ENDIAN);
    testGetBytesArray(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetBytesArray(ByteOrder order) {
    // Given: an array of bytes
    int offset = 4;
    int size = 4;
    byte[] bytes = order == ByteOrder.BIG_ENDIAN ? bigEndianBytes : littleEndianBytes;
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;
    byte[] expected = Arrays.copyOfRange(bytes, offset, offset + size);

    // When: the array is extracted
    byte[] actual = buf.getBytes(offset, size);

    // Then: the byte arrays are the same
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getBytes()}.
   */
  @Test
  void testGetAllBytes() {
    testGetAllBytes(ByteOrder.BIG_ENDIAN);
    testGetAllBytes(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetAllBytes(ByteOrder order) {
    // Given: an array of bytes
    byte[] expected = order == ByteOrder.BIG_ENDIAN ? bigEndianBytes : littleEndianBytes;
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;

    // When: the array is returned
    byte[] actual = buf.getBytes();

    // Then: the byte arrays are the same
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link ByteOrderBuffer#getTimestamp(int)}.
   */
  @Test
  void testGetTimestamp() {
    testGetTimestamp(ByteOrder.BIG_ENDIAN);
    testGetTimestamp(ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param order
   */
  private void testGetTimestamp(ByteOrder order) {
    // Given: a timestamp in a buffer
    LocalDateTime expected = timestamp;
    ByteOrderBuffer buf = order == ByteOrder.BIG_ENDIAN ? bigEndianBuffer : littleEndianBuffer;

    // When: the timestamp is retrieved
    LocalDateTime actual = buf.getTimestamp(timestampPos);

    // Then: the timestamps are the same
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test method for {@link com.goosebumpdesigns.pe.file.ByteOrderBuffer#getByteOrder()}.
   */
  @Test
  void testGetByteOrder() {
    testGetByteORder(bigEndianBuffer, ByteOrder.BIG_ENDIAN);
    testGetByteORder(littleEndianBuffer, ByteOrder.LITTLE_ENDIAN);
  }

  /**
   * @param buffer
   * @param expected
   */
  private void testGetByteORder(ByteOrderBuffer buffer, ByteOrder expected) {
    // Given: a byte order buffer

    // When: the order of the buffer is returned.
    ByteOrder actual = buffer.getByteOrder();

    // Then: the value is as expected.
    assertThat(actual).isEqualTo(expected);
  }
}
