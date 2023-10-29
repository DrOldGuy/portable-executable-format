// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe;

import static com.goosebumpdesigns.pe.model.type.FieldData.value;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.model.type.FieldData;
import com.goosebumpdesigns.pe.model.type.HeaderCharacteristic;
import com.goosebumpdesigns.pe.model.type.HeaderField;
import com.goosebumpdesigns.pe.model.type.MachineType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Getter
@ToString
@EqualsAndHashCode
public class PEHeader {
  // @formatter:off
  private static final Map<Enum<?>, FieldData> headerData = Map.ofEntries(
      value(HeaderField.MACHINE, 0, 2), 
      value(HeaderField.NUMBER_OF_SECTIONS, 2, 2),
      value(HeaderField.TIME_DATE_STAMP, 4, 4), 
      value(HeaderField.POINTER_TO_SYMBOL_TABLE, 8, 4),
      value(HeaderField.NUMBER_OF_SYMBOLS, 12, 4),
      value(HeaderField.SIZE_OF_OPTIONAL_HEADER, 16, 2),
      value(HeaderField.CHARACTERISTICS, 18, 2)
  );
  // @formatter:on

  private MachineType machineType;
  private int numberOfSections;
  private LocalDateTime timestamp;
  private int symbolTableOffset;
  private int numberOfSymbols;
  private int sizeOfOptionalHeader;
  private List<HeaderCharacteristic> characteristics;

  /**
   * Create and initialize the header object.
   * 
   * @param buffer
   */
  public PEHeader(ByteOrderBuffer buffer) {
    machineType = readMachineType(buffer);
    numberOfSections = readNumberOfSections(buffer);
    timestamp = readTimestamp(buffer);
    symbolTableOffset = readSymbolTableOffset(buffer);
    numberOfSymbols = readNumberOfSymbols(buffer);
    sizeOfOptionalHeader = readSizeOfOptionalHeader(buffer);
    characteristics = readCharacteristics(buffer);
  }

  /**
   * @param buffer
   * @return
   */
  private List<HeaderCharacteristic> readCharacteristics(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.CHARACTERISTICS);
    short value = buffer.readShort(data.getOffset());
    return HeaderCharacteristic.allCharacteristicsIn(value);
  }

  /**
   * @param buffer
   * @return
   */
  private int readSizeOfOptionalHeader(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.SIZE_OF_OPTIONAL_HEADER);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int readNumberOfSymbols(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.NUMBER_OF_SYMBOLS);
    return buffer.readInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int readSymbolTableOffset(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.POINTER_TO_SYMBOL_TABLE);
    return buffer.readInt(data.getOffset());
  }

  /**
   * The timestamp is stored in the DLL as the number of milliseconds since 01-Jan-1970. The
   * specification doesn't say what time zone the timestamp is in so this method assumes UTC.
   *
   * @param content
   * @param offset
   * @return
   */
  private LocalDateTime readTimestamp(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.TIME_DATE_STAMP);
    return buffer.readTimestamp(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int readNumberOfSections(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.NUMBER_OF_SECTIONS);
    return buffer.readShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private MachineType readMachineType(ByteOrderBuffer buffer) {
    FieldData data = headerData.get(HeaderField.MACHINE);
    int value = buffer.readShort(data.getOffset());
    return MachineType.valueOf(value);
  }

}
