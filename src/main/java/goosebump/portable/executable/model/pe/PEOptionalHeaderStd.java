// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.model.PEFieldData.value;
import java.util.Map;
import goosebump.portable.executable.file.ByteOrderBuffer;
import goosebump.portable.executable.model.PEFieldData;
import goosebump.portable.executable.model.PEOptionalHeaderField;

/**
 * 
 */
public class PEOptionalHeaderStd extends PEOptionalHeader {

  // @formatter:off
  private Map<Enum<?>, PEFieldData> headerData = Map.ofEntries(
      value(PEOptionalHeaderField.MAGIC_NUMBER, 0,2),
      value(PEOptionalHeaderField.MAJOR_LINKER_VERSION, 2, 1),
      value(PEOptionalHeaderField.MINOR_LINKER_VERSION, 3, 1),
      value(PEOptionalHeaderField.SIZE_OF_CODE, 4, 4),
      value(PEOptionalHeaderField.SIZE_OF_INITIALIZED_DATA, 8, 4),
      value(PEOptionalHeaderField.SIZE_OF_UNINITIALIZED_DATA, 12, 4),
      value(PEOptionalHeaderField.ADDRESS_OF_ENTRY_POINT, 16, 4),
      value(PEOptionalHeaderField.BASE_OF_CODE, 20, 4),
      value(PEOptionalHeaderField.BASE_OF_DATA, 24, 4),
      value(PEOptionalHeaderField.IMAGE_BASE, 28, 4),
      value(PEOptionalHeaderField.SECTION_ALIGNMENT, 32, 4),
      value(PEOptionalHeaderField.FILE_ALIGNMENT, 36, 4),
      value(PEOptionalHeaderField.MAJOR_OPERATING_SYSTEM_VERSION, 40, 2),
      value(PEOptionalHeaderField.MINOR_OPERATING_SYSTEM_VERSION, 42, 2),
      value(PEOptionalHeaderField.MAJOR_IMAGE_VERSION, 44, 2),
      value(PEOptionalHeaderField.MINOR_IMAGE_VERSION, 46, 2),
      value(PEOptionalHeaderField.MAJOR_SUBSYSTEM_VERSION, 48, 2),
      value(PEOptionalHeaderField.MINOR_SUBSYSTEM_VERSION, 50, 2),
      value(PEOptionalHeaderField.WIN32_VERSION_VALUE, 52, 4),
      value(PEOptionalHeaderField.SIZE_OF_IMAGE, 56, 4),
      value(PEOptionalHeaderField.SIZE_OF_HEADERS, 60, 4),
      value(PEOptionalHeaderField.CHECKSUM, 64, 4),
      value(PEOptionalHeaderField.SUBSYSTEM, 68, 2),
      value(PEOptionalHeaderField.DLL_CHARACTERISTICS, 70, 2),
      value(PEOptionalHeaderField.SIZE_OF_STACK_RESERVE, 72, 4),
      value(PEOptionalHeaderField.SIZE_OF_STACK_COMMIT, 76, 4),
      value(PEOptionalHeaderField.SIZE_OF_HEAP_RESERVE, 80, 4),
      value(PEOptionalHeaderField.SIZE_OF_HEAP_COMMIT, 84, 4),
      value(PEOptionalHeaderField.LOADER_FLAGS, 88, 4),
      value(PEOptionalHeaderField.NUMBER_OF_RVA_AND_SIZES, 92, 4),
      value(PEOptionalHeaderField.EXPORT_TABLE, 96, 8),
      value(PEOptionalHeaderField.IMPORT_TABLE, 104, 8),
      value(PEOptionalHeaderField.RESOURCE_TABLE, 112, 8),
      value(PEOptionalHeaderField.EXCEPTION_TABLE, 120, 8),
      value(PEOptionalHeaderField.CERTIFICATE_TABLE, 128, 8),
      value(PEOptionalHeaderField.BASE_RELOCATION_TABLE, 136, 8),
      value(PEOptionalHeaderField.DEBUG_DATA, 144, 8),
      value(PEOptionalHeaderField.ARCHITECTURE, 152, 8),
      value(PEOptionalHeaderField.GLOBAL_POINTER, 160, 8),
      value(PEOptionalHeaderField.THREAD_LOCAL_STORAGE_TABLE, 168, 8),
      value(PEOptionalHeaderField.LOAD_CONFIGURATION_TABLE, 176, 8),
      value(PEOptionalHeaderField.BOUND_IMPORT_TABLE, 184, 8),
      value(PEOptionalHeaderField.IMPORT_ADDRESS_TABLE, 192, 8),
      value(PEOptionalHeaderField.DELAY_IMPORT_DESCRIPTOR, 200, 8),
      value(PEOptionalHeaderField.CLR_RUNTIME_HEADER, 208, 8),
      value(PEOptionalHeaderField.RESERVED, 216, 8)
  );
  // @formatter:on

  /**
   * @param buffer
   */
  public PEOptionalHeaderStd(ByteOrderBuffer buffer) {
    parseAndLoadOptionalHeader(buffer);
  }

  /**
   * 
   */
  @Override
  protected PEFieldData dataForField(PEOptionalHeaderField field) {
    return headerData.get(field);
  }

}
