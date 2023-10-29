// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.builder;

import java.nio.file.Path;
import com.goosebumpdesigns.pe.PEExports;
import com.goosebumpdesigns.pe.PEFile;
import com.goosebumpdesigns.pe.PEHeader;
import com.goosebumpdesigns.pe.PEOptionalHeader;
import com.goosebumpdesigns.pe.PESectionTable;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.file.PEFileReader;
import com.goosebumpdesigns.pe.file.SectionTableBuffer;
import com.goosebumpdesigns.pe.file.PEFileReader.ExportBuffers;
import com.goosebumpdesigns.pe.optionalheader.OptionalHeaderFactory;

/**
 * 
 */
public class PEFileBuilder {
  public static PEFile build(Path path) {
    return new PEFileBuilder().newPEFile(path);
  }

  /**
   * @param path
   * @return
   */
  private PEFile newPEFile(Path path) {
    try(PEFileReader reader = new PEFileReader(path)) {
      PEHeader header = readHeader(reader);
      PEOptionalHeader optionalHeader = readOptionalHeader(reader);
      PESectionTable sectionTable = readSectionTable(reader);
      PEExports exports = readExports(reader);

      // @formatter:off
      return PEFile.builder()
          .header(header)
          .optionalHeader(optionalHeader)
          .sectionTable(sectionTable)
          .exports(exports)
          .build();
      // @formatter:on
    }
  }

  /**
   * @param reader
   * @return
   */
  private PEExports readExports(PEFileReader reader) {
    ExportBuffers buffers = reader.readExportBuffers();
    return new PEExports(buffers.getHeader(), buffers.getFileName(), buffers.getExportNames());
  }

  /**
   * 
   * @param reader
   * @return
   */
  private PEHeader readHeader(PEFileReader reader) {
    ByteOrderBuffer buffer = reader.readHeaderBuffer();
    return new PEHeader(buffer);
  }

  /**
   * 
   * @param reader
   * @return
   */
  private PEOptionalHeader readOptionalHeader(PEFileReader reader) {
    ByteOrderBuffer buffer = reader.readOptionalBuffer();
    return OptionalHeaderFactory.createOptionalHeader(buffer);
  }

  /**
   * 
   * @param reader
   * @return
   */
  private PESectionTable readSectionTable(PEFileReader reader) {
    SectionTableBuffer buffer = reader.readSectionTableBuffer();
    return new PESectionTable(buffer);
  }

}
