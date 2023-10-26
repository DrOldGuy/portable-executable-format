// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.model.PEDirectory;
import goosebump.portable.executable.model.PEDirectoryTable;
import goosebump.portable.executable.model.PEFieldData;
import goosebump.portable.executable.model.PEMagicNumber;
import goosebump.portable.executable.model.PEMemSize;
import goosebump.portable.executable.model.PEOptionalHeaderCharacteristic;
import goosebump.portable.executable.model.PEOptionalHeaderField;
import goosebump.portable.executable.model.PEVersion;
import goosebump.portable.executable.model.PEWindowsSubsystem;
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

  protected abstract PEFieldData dataForField(PEOptionalHeaderField field);

  private PEMagicNumber magicNumber;
  private PEVersion linkerVersion;
  private long codeSize;
  private long initializedDataSize;
  private long uninitializedDataSize;
  private long entryPointAddress;
  private long codeBaseAddress;
  private long dataBaseAddress;
  private BigInteger imageBase;
  private long sectionAlignment;
  private long fileAlignment;
  private PEVersion operatingSystemVersion;
  private PEVersion imageVersion;
  private PEVersion subsystemVersion;
  private long win32VersionValue;
  private long imageSize;
  private long headerSize;
  private long checksum;
  private PEWindowsSubsystem subsystem;
  private List<PEOptionalHeaderCharacteristic> characteristics;
  private PEMemSize stackMemory;
  private PEMemSize heapMemory;
  private long loaderFlags;
  private long numberOfRvaAndSizes;
  private PEDirectoryTable directoryTable;

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
  private PEDirectoryTable loadDirectoryTable(ByteOrderBuffer buffer) {
    // @formatter:off
    PEDirectory exports = loadDirectory(buffer, PEOptionalHeaderField.EXPORT_TABLE);
    PEDirectory imports = loadDirectory(buffer, PEOptionalHeaderField.IMPORT_TABLE);
    PEDirectory resources = loadDirectory(buffer, PEOptionalHeaderField.RESOURCE_TABLE);
    PEDirectory exceptions = loadDirectory(buffer, PEOptionalHeaderField.EXCEPTION_TABLE);
    PEDirectory certificates = loadDirectory(buffer, PEOptionalHeaderField.CERTIFICATE_TABLE);
    PEDirectory relocations = loadDirectory(buffer, PEOptionalHeaderField.BASE_RELOCATION_TABLE);
    PEDirectory debugData = loadDirectory(buffer, PEOptionalHeaderField.DEBUG_DATA);
    PEDirectory architecture = loadDirectory(buffer, PEOptionalHeaderField.ARCHITECTURE);
    PEDirectory globalPointer = loadDirectory(buffer, PEOptionalHeaderField.GLOBAL_POINTER);
    PEDirectory threadLocalStorage = loadDirectory(buffer, PEOptionalHeaderField.THREAD_LOCAL_STORAGE_TABLE);
    PEDirectory loadConfiguration = loadDirectory(buffer, PEOptionalHeaderField.LOAD_CONFIGURATION_TABLE);
    PEDirectory boundImport = loadDirectory(buffer, PEOptionalHeaderField.BOUND_IMPORT_TABLE);
    PEDirectory importAddress = loadDirectory(buffer, PEOptionalHeaderField.IMPORT_ADDRESS_TABLE);
    PEDirectory delayImportDescriptor = loadDirectory(buffer, PEOptionalHeaderField.DELAY_IMPORT_DESCRIPTOR);
    PEDirectory clrRuntimeHeader = loadDirectory(buffer, PEOptionalHeaderField.CLR_RUNTIME_HEADER);
    PEDirectory reserved = loadDirectory(buffer, PEOptionalHeaderField.RESERVED);
    // @formatter:on

    // @formatter:off
    return PEDirectoryTable.builder()
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
  private PEDirectory loadDirectory(ByteOrderBuffer buffer, PEOptionalHeaderField name) {
    PEFieldData data = dataForField(name);
    long address = buffer.readUnsignedInt(data.getOffset());
    long size = buffer.readUnsignedInt(data.getOffset() + Integer.BYTES);

    return new PEDirectory(address, size);
  }

  /**
   * @param buffer
   * @return
   */
  private PEMemSize loadHeapMemorySize(ByteOrderBuffer buffer) {
    BigInteger reserve = loadHeapReserveSize(buffer);
    BigInteger commit = loadHeapCommitSize(buffer);

    return new PEMemSize(reserve, commit);
  }

  /**
   * @param buffer
   * @return
   */
  private PEMemSize loadStackMemorySize(ByteOrderBuffer buffer) {
    BigInteger reserve = loadStackReserveSize(buffer);
    BigInteger commit = loadStackCommitSize(buffer);

    return new PEMemSize(reserve, commit);
  }

  /**
   * @param buffer
   * @return
   */
  private PEVersion loadSubsystemVersion(ByteOrderBuffer buffer) {
    int major = loadMajorSubsystemVersion(buffer);
    int minor = loadMinorSubsystemVersion(buffer);

    return new PEVersion(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private PEVersion loadImageVersion(ByteOrderBuffer buffer) {
    int major = loadMajorImageVersion(buffer);
    int minor = loadMinorImageVersion(buffer);

    return new PEVersion(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private PEVersion loadOperatingSystemVersion(ByteOrderBuffer buffer) {
    int major = loadMajorOperatingSystemVersion(buffer);
    int minor = loadMinorOperatingSystemVersion(buffer);

    return new PEVersion(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private PEVersion loadLinkerVersion(ByteOrderBuffer buffer) {
    int major = loadMajorLinkerVersion(buffer);
    int minor = loadMinorLinkerVersion(buffer);

    return new PEVersion(major, minor);
  }

  /**
   * @param buffer
   * @return
   */
  private long loadNumberOfRvaAndSizes(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.NUMBER_OF_RVA_AND_SIZES);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadLoaderFlags(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.LOADER_FLAGS);
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
    return readUnsignedLongOrInt(buffer, PEOptionalHeaderField.SIZE_OF_HEAP_COMMIT);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadHeapReserveSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, PEOptionalHeaderField.SIZE_OF_HEAP_RESERVE);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadStackCommitSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, PEOptionalHeaderField.SIZE_OF_STACK_COMMIT);
  }

  /**
   * @param buffer
   * @return
   */
  protected BigInteger loadStackReserveSize(ByteOrderBuffer buffer) {
    return readUnsignedLongOrInt(buffer, PEOptionalHeaderField.SIZE_OF_STACK_RESERVE);
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger readUnsignedLongOrInt(ByteOrderBuffer buffer, PEOptionalHeaderField field) {
    PEFieldData data = dataForField(field);

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
  private List<PEOptionalHeaderCharacteristic> loadCharacteristics(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.DLL_CHARACTERISTICS);
    int value = buffer.readUnsignedShort(data.getOffset());

    return PEOptionalHeaderCharacteristic.allCharacteristicsIn(value);
  }

  /**
   * @param buffer
   * @return
   */
  private PEWindowsSubsystem loadSubsystem(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SUBSYSTEM);
    int value = buffer.readUnsignedShort(data.getOffset());
    return PEWindowsSubsystem.valueOf(value);
  }

  /**
   * @param buffer
   * @return
   */
  private long loadChecksum(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.CHECKSUM);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadHeaderSize(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SIZE_OF_HEADERS);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadImageSize(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SIZE_OF_IMAGE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadWin32VersionValue(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.WIN32_VERSION_VALUE);
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
    PEFieldData data = dataForField(PEOptionalHeaderField.MINOR_SUBSYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorSubsystemVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MAJOR_SUBSYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorImageVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MINOR_IMAGE_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorImageVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MAJOR_IMAGE_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorOperatingSystemVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MINOR_OPERATING_SYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorOperatingSystemVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MAJOR_OPERATING_SYSTEM_VERSION);
    return buffer.readUnsignedShort(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadFileAllignment(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.FILE_ALIGNMENT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadSectionAlignment(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SECTION_ALIGNMENT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private BigInteger loadImageBase(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.IMAGE_BASE);
    return buffer.readUnsignedLong(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadDataBaseAddress(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.BASE_OF_DATA);
    return Objects.nonNull(data) ? buffer.readUnsignedInt(data.getOffset()) : 0;
  }

  /**
   * @param buffer
   * @return
   */
  private long loadCodeBaseAddress(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.BASE_OF_CODE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadEntryPointAddress(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.ADDRESS_OF_ENTRY_POINT);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadUninitializedDataSize(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SIZE_OF_UNINITIALIZED_DATA);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadInitializedDataSize(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SIZE_OF_INITIALIZED_DATA);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private long loadCodeSize(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.SIZE_OF_CODE);
    return buffer.readUnsignedInt(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMinorLinkerVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MINOR_LINKER_VERSION);
    return buffer.readUnsignedByte(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private int loadMajorLinkerVersion(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MAJOR_LINKER_VERSION);
    return buffer.readUnsignedByte(data.getOffset());
  }

  /**
   * @param buffer
   * @return
   */
  private PEMagicNumber loadMagicNumber(ByteOrderBuffer buffer) {
    PEFieldData data = dataForField(PEOptionalHeaderField.MAGIC_NUMBER);
    int value = buffer.readUnsignedShort(data.getOffset());

    return PEMagicNumber.valueOf(value);
  }
}
