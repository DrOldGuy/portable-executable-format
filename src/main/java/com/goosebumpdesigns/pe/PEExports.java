// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe;

import static com.goosebumpdesigns.pe.model.type.FieldData.value;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import com.goosebumpdesigns.pe.exception.PEFileException;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.model.Export;
import com.goosebumpdesigns.pe.model.Version;
import com.goosebumpdesigns.pe.model.type.ExportDirectoryField;
import com.goosebumpdesigns.pe.model.type.FieldData;
import lombok.Getter;

/**
 * 
 */
@Getter
public class PEExports {
  // @formatter:off
  private static final Map<Enum<?>, FieldData> fields = Map.ofEntries(
      value(ExportDirectoryField.EXPORT_FLAGS, 0, 4),
      value(ExportDirectoryField.TIMESTAMP, 4, 4),
      value(ExportDirectoryField.MAJOR_VERSION, 8, 2),
      value(ExportDirectoryField.MINOR_VERSION, 10, 2),
      value(ExportDirectoryField.NAME_RVA, 12, 4),
      value(ExportDirectoryField.ORDINAL_BASE, 16, 4),
      value(ExportDirectoryField.NUMBER_OF_ADDRESS_TABLE_ENTRIES, 20, 4),
      value(ExportDirectoryField.NUMBER_OF_NAME_POINTERS, 24, 4),
      value(ExportDirectoryField.EXPORT_ADDRESS_TABLE_RVA, 28, 4),
      value(ExportDirectoryField.NAME_POINTER_RVA, 32, 4),
      value(ExportDirectoryField.ORDINAL_TABLE_RVA, 36, 4)
  );
  // @formatter:on

  private int exportFlags;
  private LocalDateTime timestamp;
  private Version version;
  private String name;
  private long ordinalBase;
  private long addressTableEntries;
  private long numberOfNames;
  private long exportAddressTableRva;
  private long nameTableRva;
  private long ordinalTableRva;
  private Set<Export> exportNames;

  /**
   * @param buffers
   */
  public PEExports(ByteOrderBuffer headerBuffer, String fileName, Set<Export> exportNames) {
    this.exportFlags = readFlags(headerBuffer);
    this.timestamp = readTimestamp(headerBuffer);
    this.version = readVersion(headerBuffer);
    this.name = fileName;
    this.ordinalBase = readOrdinalBase(headerBuffer);
    this.addressTableEntries = readAddressTableEntries(headerBuffer);
    this.numberOfNames = readNumberOfNames(headerBuffer);
    this.exportAddressTableRva = readExportAddressTableRva(headerBuffer);
    this.nameTableRva = readNameTableRva(headerBuffer);
    this.ordinalTableRva = readOrdinalTableRva(headerBuffer);
    this.exportNames = exportNames;
  }

  /**
   * @param header
   * @return
   */
  private long readOrdinalTableRva(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.ORDINAL_TABLE_RVA);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param header
   * @return
   */
  private long readNameTableRva(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.NAME_POINTER_RVA);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param header
   * @return
   */
  private long readExportAddressTableRva(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.EXPORT_ADDRESS_TABLE_RVA);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param header
   * @return
   */
  private long readNumberOfNames(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.NUMBER_OF_NAME_POINTERS);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param header
   * @return
   */
  private long readAddressTableEntries(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.NUMBER_OF_ADDRESS_TABLE_ENTRIES);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param header
   * @return
   */
  private long readOrdinalBase(ByteOrderBuffer header) {
    FieldData data = fields.get(ExportDirectoryField.ORDINAL_BASE);
    return header.readUnsignedInt(data.getOffset());
  }

  /**
   * @param headerBuffer
   * @return
   */
  private Version readVersion(ByteOrderBuffer headerBuffer) {
    int major = readVersionMajor(headerBuffer);
    int minor = readVersionMinor(headerBuffer);

    return new Version(major, minor);
  }

  /**
   * @param headerBuffer
   * @return
   */
  private int readVersionMajor(ByteOrderBuffer headerBuffer) {
    FieldData data = fields.get(ExportDirectoryField.MAJOR_VERSION);
    return headerBuffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param headerBuffer
   * @return
   */
  private int readVersionMinor(ByteOrderBuffer headerBuffer) {
    FieldData data = fields.get(ExportDirectoryField.MINOR_VERSION);
    return headerBuffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param headerBuffer
   * @return
   */
  private LocalDateTime readTimestamp(ByteOrderBuffer headerBuffer) {
    FieldData data = fields.get(ExportDirectoryField.TIMESTAMP);
    return headerBuffer.readTimestamp(data.getOffset());
  }

  /**
   * @param headerBuffer
   * @return
   */
  private int readFlags(ByteOrderBuffer headerBuffer) {
    FieldData data = fields.get(ExportDirectoryField.EXPORT_FLAGS);
    int flags = headerBuffer.readInt(data.getOffset());

    if(flags != 0) {
      throw new PEFileException("Export flags must be zero! Was: " + flags);
    }

    return flags;
  }
}
