// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.model.FieldData.value;
import java.util.List;
import java.util.Map;
import goosebump.portable.executable.file.SectionBuffer;
import goosebump.portable.executable.model.FieldData;
import goosebump.portable.executable.model.SectionCharacteristic;
import goosebump.portable.executable.model.SectionField;
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
  private static final Map<Enum<?>, FieldData> sectionData = Map.ofEntries(
      value(SectionField.NAME, 0, 8),
      value(SectionField.VIRTUAL_SIZE, 8, 4),
      value(SectionField.VIRTUAL_ADDRESS, 12, 4),
      value(SectionField.SIZE_OF_RAW_DATA, 16, 4),
      value(SectionField.POINTER_TO_RAW_DATA, 20, 4),
      value(SectionField.POINTER_TO_RELOCATIONS, 24, 4),
      value(SectionField.POINTER_TO_LINE_NUMBERS, 28, 4),
      value(SectionField.NUMBER_OF_RELOCATIONS, 32, 2),
      value(SectionField.NUMBER_OF_LINE_NUMBERS, 34, 2),
      value(SectionField.CHARACTERISTICS, 36, 4)
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
  private List<SectionCharacteristic> characteristics;

  /**
   * @param sectionTableBuffer
   * @param sectionOffset
   */
  public PESection(SectionBuffer sectionBuffer) {
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
  private List<SectionCharacteristic> readCharacteristics(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.CHARACTERISTICS);
    int flags = sectionBuffer.readInt(data.getOffset());
    return SectionCharacteristic.allCharacteristicsIn(flags);
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private int readNumberOfLineNumbers(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.NUMBER_OF_LINE_NUMBERS);
    return sectionBuffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private int readNumberOfRelocations(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.NUMBER_OF_RELOCATIONS);
    return sectionBuffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readLineNumbersPointer(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.POINTER_TO_LINE_NUMBERS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readRelocationsPointer(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.POINTER_TO_RELOCATIONS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readRawDataPointer(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.POINTER_TO_RAW_DATA);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readRawDataSize(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.SIZE_OF_RAW_DATA);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readVirtualAddress(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.VIRTUAL_ADDRESS);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @return
   */
  private long readVirtualSize(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.VIRTUAL_SIZE);
    return sectionBuffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param sectionBuffer
   * @param sectionOffset
   * @return
   */
  private String readSectionName(SectionBuffer sectionBuffer) {
    FieldData data = sectionData.get(SectionField.NAME);
    byte[] buffer = sectionBuffer.bytes(data.getOffset(), data.getSize());

    for(int size = 0; size < buffer.length; size++) {
      if(buffer[size] == 0) {
        return new String(buffer, 0, size);
      }
    }

    return new String(buffer);
  }

}
