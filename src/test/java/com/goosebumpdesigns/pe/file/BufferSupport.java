// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class BufferSupport {
  protected int anInt = Integer.MAX_VALUE;
  protected short aShort = Short.MAX_VALUE;
  protected byte aByte = Byte.MAX_VALUE;
  protected long aLong = Long.MAX_VALUE;
  protected int negInt = Integer.MIN_VALUE;
  protected short negShort = Short.MIN_VALUE;
  protected long negLong = Long.MIN_VALUE;
  protected byte negByte = Byte.MIN_VALUE;
  protected LocalDateTime timestamp = LocalDateTime.of(2023, 11, 14, 13, 28, 41);
  protected ZonedDateTime zonedTimestamp = timestamp.atZone(ZoneId.of("UTC"));
  protected int secondsSinceBeginningOfTime = (int)zonedTimestamp.toEpochSecond();

  protected int size = Integer.BYTES + Short.BYTES + Byte.BYTES + Integer.BYTES + Short.BYTES
      + Long.BYTES + Long.BYTES + Byte.BYTES + Integer.BYTES;
  protected int intPos = 0;
  protected int shortPos = Integer.BYTES;
  protected int bytePos = shortPos + Short.BYTES;
  protected int negIntPos = bytePos + Byte.BYTES;
  protected int negShortPos = negIntPos + Integer.BYTES;
  protected int longPos = negShortPos + Short.BYTES;
  protected int negLongPos = longPos + Long.BYTES;
  protected int negBytePos = negLongPos + Long.BYTES;
  protected int timestampPos = negBytePos + Byte.BYTES;

  /**
   * 
   */
  protected byte[] initBuffer(ByteOrder order) {
    List<Byte> byteList = new LinkedList<Byte>();

    byteList.addAll(toList(anInt, order, Integer.BYTES));
    byteList.addAll(toList(aShort, order, Short.BYTES));
    byteList.addAll(toList(aByte, order, Byte.BYTES));
    byteList.addAll(toList(negInt, order, Integer.BYTES));
    byteList.addAll(toList(negShort, order, Short.BYTES));
    byteList.addAll(toList(aLong, order, Long.BYTES));
    byteList.addAll(toList(negLong, order, Long.BYTES));
    byteList.addAll(toList(negByte, order, Byte.BYTES));
    byteList.addAll(toList(secondsSinceBeginningOfTime, order, Integer.BYTES));

    return toByteArray(byteList);
  }

  /**
   * @param value
   * @param order
   * @return
   */
  private List<Byte> toList(long value, ByteOrder order, int size) {
    byte[] buf = new byte[size];

    for(int i = 0; i < size; i++) {
      int pos = order == ByteOrder.BIG_ENDIAN ? size - i - 1 : i;
      buf[pos] = (byte)(value & 0xff);
      value = value >>> 8;
    }

    List<Byte> bytes = new ArrayList<>(size);

    for(int i = 0; i < size; i++) {
      bytes.add(buf[i]);
    }

    return bytes;
  }

  /**
   * @param byteList
   * @return
   */
  private byte[] toByteArray(List<Byte> byteList) {
    byte[] buf = new byte[byteList.size()];

    for(int i = 0; i < byteList.size(); i++) {
      buf[i] = byteList.get(i);
    }

    return buf;
  }

  /**
   * 
   */
  protected byte[] initLittleEndian() {
    List<Byte> byteList = new LinkedList<Byte>();

    return toByteArray(byteList);
  }


}
