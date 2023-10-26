// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.Constants.PE_HEADER_SIZE;
import static goosebump.portable.executable.Constants.PE_SECTION_SIZE;
import static goosebump.portable.executable.Constants.PE_SIGNATURE;
import static goosebump.portable.executable.Constants.PE_SIGNATURE_LOCATION;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;
import goosebump.portable.executable.exception.PEFileException;
import goosebump.portable.executable.exception.PESignatureException;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.file.PEReader;
import goosebump.portable.executable.model.PEMachineType;
import goosebump.portable.executable.model.pe.factory.PEOptionalHeaderFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@ToString
@EqualsAndHashCode()
public class PEFile {
  @Getter
  private PEHeader peHeader;

  @Getter
  private PEOptionalHeader peOptionalHeader;

  @Getter
  private PESectionTable peSectionTable;

  private ByteOrder byteOrder;
  private long signatureOffset;
  private long headerOffset;
  private long optionalHeaderOffset;
  private long sectionTableOffset;

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
    try (PEReader reader = new PEReader(path)) {
      readAndVerifySignature(reader);

      peHeader = readHeader(reader);
      peOptionalHeader = readOptionalHeader(reader);
      peSectionTable = readSectionTable(reader);

    } catch (IOException e) {
      throw new PEFileException(e);
    }
  }

  /**
   * 
   * @param reader
   * @return
   */
  private PEHeader readHeader(PEReader reader) {
    try {
      headerOffset = signatureOffset + PE_SIGNATURE.length;
      byte[] headerBytes = reader.readBytes(headerOffset, PE_HEADER_SIZE);

      byteOrder = checkByteOrder(headerBytes);
      ByteOrderBuffer headerBuffer = new ByteOrderBuffer(headerBytes, byteOrder);

      return new PEHeader(headerBuffer);
    } catch (IOException e) {
      throw new PEFileException(e, reader.getPath(), headerOffset, PE_HEADER_SIZE);
    }
  }

  /**
   * @param reader
   * @param signatureOffset
   * @return
   * @throws IOException
   */
  private void readAndVerifySignature(PEReader reader) {
    try {
      readSignatureOffset(reader);

      byte[] signatureBytes = reader.readBytes(signatureOffset, PE_SIGNATURE.length);

      if (!Arrays.equals(signatureBytes, PE_SIGNATURE)) {
        throw new PESignatureException(
            reader.getPath() + " is not a valid DLL (signature mismatch)");
      }

    } catch (IOException e) {
      throw new PEFileException(e, reader.getPath(), headerOffset, PE_SIGNATURE.length);
    }

  }

  /**
   * @param reader
   * @throws IOException
   */
  private void readSignatureOffset(PEReader reader) {
    try {
      signatureOffset = reader.readUnsignedByteAt(PE_SIGNATURE_LOCATION);
    } catch (IOException e) {
      throw new PEFileException(e, reader.getPath(), PE_SIGNATURE_LOCATION, Byte.BYTES);
    }
  }

  /**
   * @param reader
   * @param peHeaderOffset
   * @param byteOrder
   * @param peHeader
   * @return
   */
  private PESectionTable readSectionTable(PEReader reader) {
    sectionTableOffset = optionalHeaderOffset + peHeader.getSizeOfOptionalHeader();
    int sectionTableLength = peHeader.getNumberOfSections() * PE_SECTION_SIZE;

    try {
      byte[] sectionTableBytes = reader.readBytes(sectionTableOffset, sectionTableLength);
      ByteOrderBuffer sectionTableBuffer = new ByteOrderBuffer(sectionTableBytes, byteOrder);
      return new PESectionTable(sectionTableBuffer, peHeader.getNumberOfSections());

    } catch (IOException e) {
      throw new PEFileException(e, reader.getPath(), sectionTableOffset, sectionTableLength);
    }
  }

  /**
   * @param reader
   * @param peHeaderOffset
   * @param byteOrder
   */
  private PEOptionalHeader readOptionalHeader(PEReader reader) {
    try {
      optionalHeaderOffset = headerOffset + PE_HEADER_SIZE;

      byte[] buffer = new byte[peHeader.getSizeOfOptionalHeader()];

      reader.seek(optionalHeaderOffset);
      reader.readFully(buffer);

      ByteOrderBuffer optionalHeaderBuffer = new ByteOrderBuffer(buffer, byteOrder);
      return PEOptionalHeaderFactory.createOptionalHeader(optionalHeaderBuffer);

    } catch (IOException e) {
      throw new PEFileException(e);
    }

  }

  /**
   * @param content
   */
  private ByteOrder checkByteOrder(byte[] content) {
    PEMachineType type = readMachineType(content);

    if (type == PEMachineType.IMAGE_FILE_MACHINE_UNKNOWN) {
      return ByteOrder.LITTLE_ENDIAN;
    }

    return ByteOrder.BIG_ENDIAN;
  }

  /**
   * @param content
   * @return
   */
  private PEMachineType readMachineType(byte[] content) {
    ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);

    buffer.put(content, PE_SIGNATURE.length, Short.BYTES);
    buffer.rewind();
    short value = buffer.getShort();

    return PEMachineType.valueOf(value);
  }
}
