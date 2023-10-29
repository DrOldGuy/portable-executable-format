// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import goosebump.pe.file.ByteOrderBuffer;
import goosebump.pe.model.Directory;
import goosebump.pe.model.DirectoryTable;
import goosebump.pe.model.MemSize;
import goosebump.pe.model.Version;
import goosebump.pe.model.type.FieldData;
import goosebump.pe.model.type.MagicNumber;
import goosebump.pe.model.type.OptionalHeaderCharacteristic;
import goosebump.pe.model.type.OptionalHeaderField;
import goosebump.pe.model.type.WindowsSubsystem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class PEOptionalHeader {

  protected abstract FieldData dataForField(OptionalHeaderField field);

  private MagicNumber magicNumber;
  private Version linkerVersion;
  private long codeSize;
  private long initializedDataSize;
  private long uninitializedDataSize;
  private long entryPointAddress;
  private long codeBaseAddress;
  private long dataBaseAddress;
  private BigInteger imageBase;
  private long sectionAlignment;
  private long fileAlignment;
  private Version operatingSystemVersion;
  private Version imageVersion;
  private Version subsystemVersion;
  private long win32VersionValue;
  private long imageSize;
  private long headerSize;
  private long checksum;
  private WindowsSubsystem subsystem;
  private List<OptionalHeaderCharacteristic> characteristics;
  private MemSize stackMemory;
  private MemSize heapMemory;
  private long loaderFlags;
  private long numberOfRvaAndSizes;
  private DirectoryTable directoryTable;

  /**
   * @param buffer
   */
  protected void parseAndLoadOptionalHeader(ByteOrderBuffer buffer) {
    magicNumber = loadMagicNumber(buffer);
    linkerVersion = loadLinkerVersion(buffer);
    codeSize = loadCodeSize(buffer);
    initializedDataSize = loadInitializedDataSize(buffer);
    uninitializedDataSize = loadUninitializedDataSize(buffer);
    entryPointAddress = loadEntryPointAddress(buffer);
    codeBaseAddress = loadCodeBaseAddress(buffer);
    dataBaseAddress = loadDataBaseAddress(buffer);
    imageBase = loadImageBase(buffer);
    sectionAlignment = loadSectionAlignment(buffer);
    fileAlignment = loadFileAllignment(buffer);
    operatingSystemVersion = loadOperatingSystemVersion(buffer);
    imageVersion = loadImageVersion(buffer);
    subsystemVersion = loadSubsystemVersion(buffer);
    win32VersionValue = loadWin32VersionValue(buffer);
    imageSize = loadImageSize(buffer);
    headerSize = loadHeaderSize(buffer);
    checksum = loadChecksum(buffer);
    subsystem = loadSubsystem(buffer);
    characteristics = loadCharacteristics(buffer);
    stackMemory = loadStackMemorySize(buffer);
    heapMemory = loadHeapMemorySize(buffer);
    loaderFlags = loadLoaderFlags(buffer);
    numberOfRvaAndSizes = loadNumberOfRvaAndSizes(buffer);
    directoryTable = loadDirectoryTable(buffer);
  }

  /**
   * @param buffer
   * @return
   */
  private DirectoryTable loadDirectoryTable(ByteOrderBuffer buffer) {
    // @formatter:off
    Directory exports = loadDirectory(buffer, OptionalHeaderField.EXPORT_TABLE);
    Directory imports = loadDirectory(buffer, OptionalHeaderField.IMPORT_TABLE);
    Directory resources = loadDirectory(buffer, OptionalHeaderField.RESOURCE_TABLE);
    Directory exceptions = loadDirectory(buffer, OptionalHeaderField.EXCEPTION_TABLE);
    Directory certificates = loadDirectory(buffer, OptionalHeaderField.CERTIFICATE_TABLE);
    Directory relocations = loadDirectory(buffer, OptionalHeaderField.BASE_RELOCATION_TABLE);
    Directory debugData = loadDirectory(buffer, OptionalHeaderField.DEBUG_DATA);
    Directory architecture = loadDirectory(buffer, OptionalHeaderField.ARCHITECTURE);
    Directory globalPointer = loadDirectory(buffer, OptionalHeaderField.GLOBAL_POINTER);
    Directory threadLocalStorage = loadDirectory(buffer, OptionalHeaderField.THREAD_LOCAL_STORAGE_TABLE);
    Directory loadConfiguration = loadDirectory(buffer, OptionalHeaderField.LOAD_CONFIGURATION_TABLE);
    Directory boundImport = loadDirectory(buffer, OptionalHeaderField.BOUND_IMPORT_TABLE);
    Directory importAddress = loadDirectory(buffer, OptionalHeaderField.IMPORT_ADDRESS_TABLE);
    Directory delayImportDescriptor = loadDirectory(buffer, OptionalHeaderField.DELAY_IMPORT_DESCRIPTOR);
    Directory clrRuntimeHeader = loadDirectory(buffer, OptionalHeaderField.CLR_RUNTIME_HEADER);
    Directory reserved = loadDirectory(buffer, OptionalHeaderField.RESERVED);
    // @formatter:on

    // @formatter:off
    return DirectoryTable.builder()
        .exports(exports)
        .imports(imports)
        .resources(resources)
        .exceptions(exceptions)
        .certificates(certificates)
        .relocations(relocations)
        .debugData(debugData)
        .architecture(architecture)
        .globalPointer(globalPointer)
        .threadLocalStorage(threadLocalStorage)
        .loadConfiguration(loadConfiguration)
        .boundImport(boundImport)
        .importAddress(importAddress)
        .delayImportDescriptor(delayImportDescriptor)
        .clrRuntimeHeader(clrRuntimeHeader)
        .reserved(reserved)
        .build();
    // @formatter:on
  }

  /**
   * @param name
   * @return
   */
  private Directory loadDirectory(ByteOrderBuffer buffer, OptionalHeaderField name) {
    FieldData data = dataForField(name);
    long address = buffer.readUnsignedInt(data.getOffset());
    long size = buffer.readUnsignedInt(data.getOffset() + Integer.BYTES);

    return new Directory(address, size);
  }

  /**
   * @param buffer
   * @return
   */
  private MemSize loadHeapMemorySize(ByteOrderBuffer buffer) {
    BigInteger reserve = loadHeapReserveSize(buffer);
    BigInteger commit = loadHeapCommitSize(buffer);

    return new MemSize(reserve, commit);
  }

  /**
   * @param buffer
   * @return
   */
  private MemSize loadStackMemorySize(ByteOrderBuffer buffer) {
    BigInteger reserve = loadStackReserveSize(buffer);
    BigInteger commit = loadStackCommitSize(buffer);

    return new MemSize(reserve, commit);
  }

  /**
   * @param buffer
   * @return
   */
  private Version loadSubsystemVersion(ByteOrderBuffer buffer) {
    int major = loadMajorSubsystemVersion(buffer);
    int minor = loadMinorSubsystemVersion(buffer);

    return new Version(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private Version loadImageVersion(ByteOrderBuffer buffer) {
    int major = loadMajorImageVersion(buffer);
    int minor = loadMinorImageVersion(buffer);

    return new Version(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private Version loadOperatingSystemVersion(ByteOrderBuffer buffer) {
    int major = loadMajorOperatingSystemVersion(buffer);
    int minor = loadMinorOperatingSystemVersion(buffer);

    return new Version(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private Version loadLinkerVersion(ByteOrderBuffer buffer) {
    int major = loadMajorLinkerVersion(buffer);
    int minor = loadMinorLinkerVersion(buffer);

    return new Version(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private long loadNumberOfRvaAndSizes(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.NUMBER_OF_RVA_AND_SIZES);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadLoaderFlags(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.LOADER_FLAGS);
    long flags = buffer.readUnsignedInt(data.getOffset());

    if (flags != 0) {
      throw new IllegalStateException("Loader flags must be zero!");
    }

    return flags;
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadHeapCommitSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, OptionalHeaderField.SIZE_OF_HEAP_COMMIT);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadHeapReserveSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, OptionalHeaderField.SIZE_OF_HEAP_RESERVE);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadStackCommitSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, OptionalHeaderField.SIZE_OF_STACK_COMMIT);
  }

  /**
   * @param buffer
   * @return
   */
  protected BigInteger loadStackReserveSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, OptionalHeaderField.SIZE_OF_STACK_RESERVE);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger readUnsignedLongOrInt(ByteOrderBuffer buffer, OptionalHeaderField field) {
    FieldData data = dataForField(field);

    if (data.getSize() == Integer.BYTES) {
      long value = buffer.readUnsignedInt(data.getOffset());
      return new BigInteger(Long.toString(value));
    }

    return buffer.readUnsignedLong(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private List<OptionalHeaderCharacteristic> loadCharacteristics(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.DLL_CHARACTERISTICS);
    int value = buffer.readUnsignedShort(data.getOffset());

    return OptionalHeaderCharacteristic.allCharacteristicsIn(value);
  }

  /**
   * @param buffer
   * @return
   */
  private WindowsSubsystem loadSubsystem(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SUBSYSTEM);
    int value = buffer.readUnsignedShort(data.getOffset());
    return WindowsSubsystem.valueOf(value);
  }

  /**
   * @param buffer
   * @return
   */
  private long loadChecksum(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.CHECKSUM);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadHeaderSize(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SIZE_OF_HEADERS);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadImageSize(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SIZE_OF_IMAGE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadWin32VersionValue(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.WIN32_VERSION_VALUE);
    long value = buffer.readUnsignedInt(data.getOffset());

    if (value != 0) {
      throw new IllegalStateException("Win32 version must be zero!");
    }

    return value;
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorSubsystemVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MINOR_SUBSYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorSubsystemVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MAJOR_SUBSYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorImageVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MINOR_IMAGE_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorImageVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MAJOR_IMAGE_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorOperatingSystemVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MINOR_OPERATING_SYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorOperatingSystemVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MAJOR_OPERATING_SYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadFileAllignment(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.FILE_ALIGNMENT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadSectionAlignment(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SECTION_ALIGNMENT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadImageBase(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.IMAGE_BASE);
    return buffer.readUnsignedLong(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadDataBaseAddress(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.BASE_OF_DATA);
    return Objects.nonNull(data) ? buffer.readUnsignedInt(data.getOffset()) : 0;
  }

  /**
   * @param buffer
   * @return
   */
  private long loadCodeBaseAddress(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.BASE_OF_CODE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadEntryPointAddress(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.ADDRESS_OF_ENTRY_POINT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadUninitializedDataSize(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SIZE_OF_UNINITIALIZED_DATA);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadInitializedDataSize(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SIZE_OF_INITIALIZED_DATA);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadCodeSize(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.SIZE_OF_CODE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorLinkerVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MINOR_LINKER_VERSION);
    return buffer.readUnsignedByte(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorLinkerVersion(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MAJOR_LINKER_VERSION);
    return buffer.readUnsignedByte(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private MagicNumber loadMagicNumber(ByteOrderBuffer buffer) {
    FieldData data = dataForField(OptionalHeaderField.MAGIC_NUMBER);
    int value = buffer.readUnsignedShort(data.getOffset());

    return MagicNumber.valueOf(value);
  }
}
