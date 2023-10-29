// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import com.goosebumpdesigns.pe.exception.PEFileException;
import com.goosebumpdesigns.pe.exception.PESignatureException;
import com.goosebumpdesigns.pe.model.Export;
import com.goosebumpdesigns.pe.model.type.MachineType;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 * This class is an implementation of a random access file that only allows reading.
 */
@ToString
public class PEFileReader implements AutoCloseable {

  @Value
  public static class ExportBuffers {
    private ByteOrderBuffer header;
    private String fileName;
    private Set<Export> exportNames;
  }

  private enum PEType {
    STANDARD, PLUS
  }

  @Value
  private class Directory {
    long offset;
    int size;
  }

  private static final int SIGNATURE_LOCATION = 60;
  private static final byte[] SIGNATURE = {'P', 'E', 0, 0};
  private static final int HEADER_SIZE = 20;
  private static final int SECTION_SIZE = 40;
  private static final int OPTIONAL_HEADER_SIZE_OFFSET = 16;
  private static final int MACHINE_OFFSET = 0;
  private static final int NUMBER_OF_SECTIONS_OFFSET = 2;

  private static final int EXPORT_STD = 96;
  private static final int EXPORT_PLUS = 112;
  private static final int EXPORT_NAME_RVA = 12;
  private static final int EXPORT_HEADER_SIZE = 40;
  private static final int EXPORT_NUM_NAMES_OFFSET = 24;
  private static final int EXPORT_NAME_POINTER_RVA_ADDRESS = 32;
  private static final int EXPORT_ORDINAL_TABLE_OFFSET = 36;

  private static final String READ = "r";
  private RandomAccessFile reader;
  private long fileLength;

  private ByteOrder byteOrder;

  private long signatureOffset;
  private long headerOffset;
  private long optionalHeaderOffset;
  private long sectionTableOffset;
  private int optionalHeaderSize;
  private int numberOfSections;
  private PEType peType;

  @Getter
  private Path path;

  /**
   * @param reader
   * @param mode
   * @throws FileNotFoundException
   */
  public PEFileReader(Path path) {
    this.path = findRealPath(path);
    this.reader = openFile(path);
    this.fileLength = fileLength();

    findHeaderOffsets();
  }

  /**
   * @return
   */
  private long fileLength() {
    try {
      return reader.length();
    }
    catch(IOException e) {
      throw new PEFileException(e);
    }
  }

  /**
   * 
   */
  private void findHeaderOffsets() {
    signatureOffset = readAndVerifySignature();
    headerOffset = signatureOffset + SIGNATURE.length;
    optionalHeaderOffset = headerOffset + HEADER_SIZE;
    optionalHeaderSize = findOptionalHeaderSize();
    numberOfSections = findNumberOfSections();

    byteOrder = findByteOrder();
    sectionTableOffset = findSectionTableOffset();
    peType = readPEType();
  }

  /**
   * @return
   */
  private int findNumberOfSections() {
    return readUnsignedShortAt(headerOffset + NUMBER_OF_SECTIONS_OFFSET);
  }

  /**
   * @return
   */
  private int findOptionalHeaderSize() {
    return readUnsignedShortAt(headerOffset + OPTIONAL_HEADER_SIZE_OFFSET);
  }

  /**
   * @return
   */
  private long findSectionTableOffset() {
    return optionalHeaderOffset + optionalHeaderSize;
  }

  /**
   * @param offset
   * @return
   */
  private int readUnsignedShortAt(long offset) {
    byte[] bytes = readBytes(offset, Short.BYTES);
    ByteOrderBuffer buffer = new ByteOrderBuffer(bytes, byteOrder);
    return buffer.readUnsignedShort(0);
  }

  /**
   * @return
   */
  private ByteOrder findByteOrder() {
    byte[] bytes = readBytes(headerOffset + MACHINE_OFFSET, Short.BYTES);

    ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
    buffer.put(bytes);
    buffer.rewind();

    short value = buffer.getShort();
    MachineType type = MachineType.valueOf(value);

    return type == MachineType.IMAGE_FILE_MACHINE_UNKNOWN ? ByteOrder.LITTLE_ENDIAN
        : ByteOrder.BIG_ENDIAN;
  }

  /**
   * @param reader
   * @param signatureOffset
   * @return
   * @throws IOException
   */
  private long readAndVerifySignature() {
    long offset = readSignatureOffset();

    byte[] signatureBytes = readBytes(offset, SIGNATURE.length);

    if(!Arrays.equals(signatureBytes, SIGNATURE)) {
      throw new PESignatureException(getPath() + " is not a valid DLL (signature mismatch)");
    }

    return offset;
  }

  /**
   * @param reader
   * @throws IOException
   */
  private long readSignatureOffset() {
    return readUnsignedByteAt(SIGNATURE_LOCATION);
  }

  /**
   * @param path
   */
  private RandomAccessFile openFile(Path path) {
    try {
      return new RandomAccessFile(path.toFile(), READ);
    }
    catch(IOException e) {
      throw new PEFileException("Unable to open file " + this.path, e);
    }
  }

  /**
   * @param path
   */
  private Path findRealPath(Path path) {
    Path realPath;

    try {
      realPath = path.toRealPath();
    }
    catch(IOException e) {
      realPath = path;
    }

    return realPath;
  }

  /**
   * Create and populate a buffer of the given length read from the given offset.
   * 
   * @param offset The offset to start reading.
   * @param length The length to read.
   * @return The populated buffer.
   * @throws IOException
   */
  private byte[] readBytes(long offset, int length) {
    try {
      byte[] buffer = new byte[length];

      seek(offset);
      reader.readFully(buffer);

      return buffer;
    }
    catch(IOException e) {
      throw newReadError(e, offset, length);
    }
  }

  /**
   * @param peSignatureLocation
   * @return
   * @throws IOException
   */
  private int readUnsignedByteAt(long position) {
    try {
      seek(position);
      return reader.readUnsignedByte();
    }
    catch(IOException e) {
      throw newReadError(e, position, Byte.BYTES);
    }
  }

  /**
   * @param e
   * @param position
   * @param length
   * @throws PEFileException
   */
  private PEFileException newReadError(Exception e, long position, int length) {
    String msg =
        String.format("Error reading %d bytes at position %d in file %s", position, length, path);

    return new PEFileException(msg);
  }

  /**
   * 
   */
  @Override
  public void close() {
    if(Objects.nonNull(reader)) {
      try {
        reader.close();
      }
      catch(IOException e) {
        throw new PEFileException("Unable to close file " + path, e);
      }
    }
  }

  /**
   * @return
   */
  public ByteOrderBuffer readHeaderBuffer() {
    byte[] bytes = readBytes(headerOffset, HEADER_SIZE);
    return new ByteOrderBuffer(bytes, byteOrder);
  }

  /**
   * @return
   */
  public ByteOrderBuffer readOptionalBuffer() {
    byte[] bytes = readBytes(optionalHeaderOffset, optionalHeaderSize);
    return new ByteOrderBuffer(bytes, byteOrder);
  }

  /**
   * @return
   */
  public SectionTableBuffer readSectionTableBuffer() {
    byte[] bytes = readBytes(sectionTableOffset, SECTION_SIZE * numberOfSections);
    ByteOrderBuffer buffer = new ByteOrderBuffer(bytes, byteOrder);
    return new SectionTableBuffer(buffer, numberOfSections, SECTION_SIZE);
  }

  /**
   * @return
   */
  private PEType readPEType() {
    int magicNumber = readUnsignedShortAt(optionalHeaderOffset);

    return switch(magicNumber) {
      case 0x010b -> PEType.STANDARD;
      case 0x020b -> PEType.PLUS;
      default -> throw new IllegalStateException(String
          .format("Magic number was 0x%04x. Should have been 0x010b or 0x020b.", magicNumber));
    };
  }

  /**
   * @param offset
   * 
   */
  private Directory readDirectoryAt(long offset) {
    byte[] bytes = readBytes(offset, Long.BYTES);

    ByteOrderBuffer buffer = new ByteOrderBuffer(bytes, byteOrder);
    long address = buffer.readUnsignedInt(0);
    long size = buffer.readUnsignedInt(Integer.BYTES);

    return new Directory(address, (int)size);
  }

  /**
   * @return
   */
  private ByteOrderBuffer readExportsHeaderBuffer() {
    int dirOffset = peType == PEType.STANDARD ? EXPORT_STD : EXPORT_PLUS;
    long offset = optionalHeaderOffset + dirOffset;
    Directory exportsDirectory = readDirectoryAt(offset);

    byte[] exportsHeaderBuffer = readBytes(exportsDirectory.offset, EXPORT_HEADER_SIZE);
    return new ByteOrderBuffer(exportsHeaderBuffer, byteOrder);
  }

  /**
   * @return
   */
  public ExportBuffers readExportBuffers() {
    ByteOrderBuffer headerBuffer = readExportsHeaderBuffer();

    String fileName = readExportFileName(headerBuffer);
    Set<Export> exports = readExports(headerBuffer);

    return new ExportBuffers(headerBuffer, fileName, exports);
  }

  /**
   * @param headerBuffer
   * @return
   */
  private Set<Export> readExports(ByteOrderBuffer headerBuffer) {
    int numEntries = (int)headerBuffer.readUnsignedInt(EXPORT_NUM_NAMES_OFFSET);
    long namePointerTableOffset = headerBuffer.readUnsignedInt(EXPORT_NAME_POINTER_RVA_ADDRESS);
    List<Long> namePointerTable = readExportNameAddressTable(namePointerTableOffset, numEntries);
    List<Integer> ordinalTable = readExportOrdinalTable(headerBuffer, numEntries);

    Set<Export> exports = new TreeSet<>();

    for(int pos = 0; pos < numEntries; pos++) {
      long nameAddress = namePointerTable.get(pos);
      String name = readString(nameAddress);
      int ordinal = ordinalTable.get(pos);

      exports.add(new Export(name, ordinal));
    }

    return exports;
  }

  /**
   * @param headerBuffer
   * @param ordinalBase
   * @return
   */
  private List<Integer> readExportOrdinalTable(ByteOrderBuffer headerBuffer, int numEntries) {
    long tableAddress = headerBuffer.readUnsignedInt(EXPORT_ORDINAL_TABLE_OFFSET);
    byte[] ordinalBytes = readBytes(tableAddress, Short.BYTES * numEntries);
    ByteOrderBuffer ordinalBuffer = new ByteOrderBuffer(ordinalBytes, byteOrder);

    List<Integer> ordinals = new ArrayList<>();
    int offset = 0;

    for(int pos = 0; pos < numEntries; pos++) {
      int ordinal = ordinalBuffer.readUnsignedShort(offset);
      offset += Short.BYTES;
      ordinals.add(ordinal);
    }

    return ordinals;
  }

  /**
   * @param namePointerTableOffset
   * @param numNames
   * @return
   */
  private List<Long> readExportNameAddressTable(long namePointerTableOffset, int numNames) {
    List<Long> nameAddressTable = new ArrayList<>();
    byte[] addressBytes = readBytes(namePointerTableOffset, Integer.BYTES * numNames);
    ByteOrderBuffer addressBuffer = new ByteOrderBuffer(addressBytes, byteOrder);
    int offset = 0;

    for(int i = 0; i < numNames; i++) {
      long address = addressBuffer.readUnsignedInt(offset);
      offset += Integer.BYTES;
      nameAddressTable.add(address);
    }

    return nameAddressTable;
  }

  /**
   * @param headerBuffer
   */
  private String readExportFileName(ByteOrderBuffer headerBuffer) {
    long nameOffset = headerBuffer.readUnsignedInt(EXPORT_NAME_RVA);
    return readString(nameOffset);
  }

  /**
   * @param offset
   * @return
   */
  public String readString(final long offset) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    seek(offset);

    try {
      while(true) {
        byte b = reader.readByte();

        if(b == 0) {
          break;
        }

        baos.write(b);
      }
    }
    catch(IOException e) {
      throw new PEFileException(
          "Error reading null-terminated String at offset " + offset + " for file " + path, e);
    }

    return new String(baos.toByteArray());
  }

  /**
   * @param offset
   */
  private void seek(long offset) {
    try {
      reader.seek(offset);
    }
    catch(IOException e) {
      throw new PEFileException(e);
    }
  }
}
