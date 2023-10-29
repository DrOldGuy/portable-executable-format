// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe.builder;

import java.nio.file.Path;
import goosebump.pe.PEExports;
import goosebump.pe.PEFile;
import goosebump.pe.PEHeader;
import goosebump.pe.PEOptionalHeader;
import goosebump.pe.PESectionTable;
import goosebump.pe.file.ByteOrderBuffer;
import goosebump.pe.file.PEFileReader;
import goosebump.pe.file.SectionTableBuffer;
import goosebump.pe.file.PEFileReader.ExportBuffers;
import goosebump.pe.optionalheader.OptionalHeaderFactory;

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
