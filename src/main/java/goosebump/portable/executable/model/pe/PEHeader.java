// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.Constants.UTC_TIME_ZONE;
import static goosebump.portable.executable.model.PEFieldData.value;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.model.PEFieldData;
import goosebump.portable.executable.model.PEHeaderCharacteristic;
import goosebump.portable.executable.model.PEHeaderField;
import goosebump.portable.executable.model.PEMachineType;
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
  private static final Map<Enum<?>, PEFieldData> headerData = Map.ofEntries(
  // @formatter:off
      value(PEHeaderField.MACHINE, 0, 2), 
      value(PEHeaderField.NUMBER_OF_SECTIONS, 2, 2),
      value(PEHeaderField.TIME_DATE_STAMP, 4, 4), 
      value(PEHeaderField.POINTER_TO_SYMBOL_TABLE, 8, 4),
      value(PEHeaderField.NUMBER_OF_SYMBOLS, 12, 4),
      value(PEHeaderField.SIZE_OF_OPTIONAL_HEADER, 16, 2),
      value(PEHeaderField.CHARACTERISTICS, 18, 2));
  // @formatter:on

  private PEMachineType machineType;
  private int numberOfSections;
  private LocalDateTime timestamp;
  private int symbolTableOffset;
  private int numberOfSymbols;
  private int sizeOfOptionalHeader;
  private List<PEHeaderCharacteristic> characteristics;

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
  private List<PEHeaderCharacteristic> readCharacteristics(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.CHARACTERISTICS);
    short value = buffer.readShort(data.getOffset());
    return PEHeaderCharacteristic.allCharacteristicsIn(value);
  }

  /**
   * @param buffer
   * @return
   */
  private int readSizeOfOptionalHeader(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.SIZE_OF_OPTIONAL_HEADER);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int readNumberOfSymbols(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.NUMBER_OF_SYMBOLS);
    return buffer.readInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int readSymbolTableOffset(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.POINTER_TO_SYMBOL_TABLE);
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
    PEFieldData data = headerData.get(PEHeaderField.TIME_DATE_STAMP);
    long seconds = buffer.readUnsignedInt(data.getOffset());
    long millis = seconds * 1000;

    return Instant.ofEpochMilli(millis).atZone(ZoneId.of(UTC_TIME_ZONE)).toLocalDateTime();
  }

  /**
   * @param buffer
   * @return
   */
  private int readNumberOfSections(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.NUMBER_OF_SECTIONS);
    return buffer.readShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private PEMachineType readMachineType(ByteOrderBuffer buffer) {
    PEFieldData data = headerData.get(PEHeaderField.MACHINE);
    int value = buffer.readShort(data.getOffset());
    return PEMachineType.valueOf(value);
  }

}
