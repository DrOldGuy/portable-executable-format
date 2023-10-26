// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.Constants.PE_SECTION_SIZE;
import static goosebump.portable.executable.model.PEFieldData.value;
import java.util.List;
import java.util.Map;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.model.PEFieldData;
import goosebump.portable.executable.model.PESectionCharacteristic;
import goosebump.portable.executable.model.PESectionField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Each
 */
@Getter
@ToString
@EqualsAndHashCode
public class PESection {
  // @formatter:off
  private static final Map<Enum<?>, PEFieldData> sectionData = Map.ofEntries(
      value(PESectionField.NAME, 0, 8),
      value(PESectionField.VIRTUAL_SIZE, 8, 4),
      value(PESectionField.VIRTUAL_ADDRESS, 12, 4),
      value(PESectionField.SIZE_OF_RAW_DATA, 16, 4),
      value(PESectionField.POINTER_TO_RAW_DATA, 20, 4),
      value(PESectionField.POINTER_TO_RELOCATIONS, 24, 4),
      value(PESectionField.POINTER_TO_LINE_NUMBERS, 28, 4),
      value(PESectionField.NUMBER_OF_RELOCATIONS, 32, 2),
      value(PESectionField.NUMBER_OF_LINE_NUMBERS, 34, 2),
      value(PESectionField.CHARACTERISTICS, 36, 4)
  );
  // @formatter:on

  private String name;
  private long virtualSize;
  private long virtualAddress;
  private long rawDataSize;
  private long rawDataPointer;
  private long relocationsPointer;
  private long lineNumbersPointer;
  private int numberOfRelocations;
  private int numberOfLineNumbers;
  private List<PESectionCharacteristic> characteristics;

  /**
   * @param sectionTableBuffer
   * @param sectionOffset
   */
  public PESection(ByteOrderBuffer sectionTableBuffer, int sectionOffset) {
    byte[] bytes = sectionTableBuffer.bytes(sectionOffset, PE_SECTION_SIZE);
    ByteOrderBuffer sectionBuffer = new ByteOrderBuffer(bytes, sectionTableBuffer.getByteOrder());
    name = readSectionName(sectionBuffer);
    virtualSize = readVirtualSize(sectionBuffer);
    virtualAddress = readVirtualAddress(sectionBuffer);
    rawDataSize = readRawDataSize(sectionBuffer);
    rawDataPointer = readRawDataPointer(sectionBuffer);
    relocationsPointer = readRelocationsPointer(sectionBuffer);
    lineNumbersPointer = readLineNumbersPointer(sectionBuffer);
    numberOfRelocations = readNumberOfRelocations(sectionBuffer);
    numberOfLineNumbers = readNumberOfLineNumbers(sectionBuffer);
    characteristics = readCharacteristics(sectionBuffer);
  }

 /**
   * @param sectionBuffer
   * @return
   */
  private List<PESectionCharacteristic> readCharacteristics(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.CHARACTERISTICS);
    int flags = sectionBuffer.readInt(data.getOffset());
    return PESectionCharacteristic.allCharacteristicsIn(flags);
  }

/**
   * @param sectionBuffer
   * @return
   */
  private int readNumberOfLineNumbers(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.NUMBER_OF_LINE_NUMBERS);
    return sectionBuffer.readUnsignedShort(data.getOffset());
  }

/**
   * @param sectionBuffer
   * @return
   */
  private int readNumberOfRelocations(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.NUMBER_OF_RELOCATIONS);
    return sectionBuffer.readUnsignedShort(data.getOffset());
  }

/**
   * @param sectionBuffer
   * @return
   */
  private long readLineNumbersPointer(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.POINTER_TO_LINE_NUMBERS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

/**
   * @param sectionBuffer
   * @return
   */
  private long readRelocationsPointer(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.POINTER_TO_RELOCATIONS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

 /**
   * @param sectionBuffer
   * @return
   */
  private long readRawDataPointer(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.POINTER_TO_RAW_DATA);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readRawDataSize(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.SIZE_OF_RAW_DATA);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readVirtualAddress(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.VIRTUAL_ADDRESS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readVirtualSize(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.VIRTUAL_SIZE);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @param sectionOffset
   * @return
   */
  private String readSectionName(ByteOrderBuffer sectionBuffer) {
    PEFieldData data = sectionData.get(PESectionField.NAME);
    byte[] buffer = sectionBuffer.bytes(data.getOffset(), data.getSize());

    for (int size = 0; size < buffer.length; size++) {
      if (buffer[size] == 0) {
        return new String(buffer, 0, size);
      }
    }

    return new String(buffer);
  }

}
