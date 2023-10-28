// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import java.nio.file.Path;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.file.PEFileReader;
import goosebump.portable.executable.file.PEFileReader.ExportBuffers;
import goosebump.portable.executable.file.SectionTableBuffer;
import goosebump.portable.executable.model.pe.factory.OptionalHeaderFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Getter
@ToString
@EqualsAndHashCode()
public class PEFile {
  private PEHeader header;
  private PEOptionalHeader optionalHeader;
  private PESectionTable sectionTable;
  private PEExports exports;

  /**
   * Create and initialize the PEFile object.
   * 
   * @param path The path to the file.
   */
  public PEFile(Path path) {
    readandParsePEFile(path);
  }

  /**
   * Read and parse the contents of the DLL file.
   */
  private void readandParsePEFile(Path path) {
    try(PEFileReader reader = new PEFileReader(path)) {
      header = readHeader(reader);
      optionalHeader = readOptionalHeader(reader);
      sectionTable = readSectionTable(reader);
      exports = readExports(reader);
    }
  }

  /**
   * @param reader
   * @return
   */
  private PEExports readExports(PEFileReader reader) {
    ExportBuffers buffers = reader.readExportBuffers();
    return new PEExports(buffers);
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
