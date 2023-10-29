// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe.optionalheader;

import static goosebump.pe.model.type.FieldData.value;
import java.util.Map;
import goosebump.pe.PEOptionalHeader;
import goosebump.pe.file.ByteOrderBuffer;
import goosebump.pe.model.type.FieldData;
import goosebump.pe.model.type.OptionalHeaderField;

/**
 * 
 */
public class OptionalHeaderStd extends PEOptionalHeader {

  // @formatter:off
  private Map<Enum<?>, FieldData> headerData = Map.ofEntries(
      value(OptionalHeaderField.MAGIC_NUMBER, 0,2),
      value(OptionalHeaderField.MAJOR_LINKER_VERSION, 2, 1),
      value(OptionalHeaderField.MINOR_LINKER_VERSION, 3, 1),
      value(OptionalHeaderField.SIZE_OF_CODE, 4, 4),
      value(OptionalHeaderField.SIZE_OF_INITIALIZED_DATA, 8, 4),
      value(OptionalHeaderField.SIZE_OF_UNINITIALIZED_DATA, 12, 4),
      value(OptionalHeaderField.ADDRESS_OF_ENTRY_POINT, 16, 4),
      value(OptionalHeaderField.BASE_OF_CODE, 20, 4),
      value(OptionalHeaderField.BASE_OF_DATA, 24, 4),
      value(OptionalHeaderField.IMAGE_BASE, 28, 4),
      value(OptionalHeaderField.SECTION_ALIGNMENT, 32, 4),
      value(OptionalHeaderField.FILE_ALIGNMENT, 36, 4),
      value(OptionalHeaderField.MAJOR_OPERATING_SYSTEM_VERSION, 40, 2),
      value(OptionalHeaderField.MINOR_OPERATING_SYSTEM_VERSION, 42, 2),
      value(OptionalHeaderField.MAJOR_IMAGE_VERSION, 44, 2),
      value(OptionalHeaderField.MINOR_IMAGE_VERSION, 46, 2),
      value(OptionalHeaderField.MAJOR_SUBSYSTEM_VERSION, 48, 2),
      value(OptionalHeaderField.MINOR_SUBSYSTEM_VERSION, 50, 2),
      value(OptionalHeaderField.WIN32_VERSION_VALUE, 52, 4),
      value(OptionalHeaderField.SIZE_OF_IMAGE, 56, 4),
      value(OptionalHeaderField.SIZE_OF_HEADERS, 60, 4),
      value(OptionalHeaderField.CHECKSUM, 64, 4),
      value(OptionalHeaderField.SUBSYSTEM, 68, 2),
      value(OptionalHeaderField.DLL_CHARACTERISTICS, 70, 2),
      value(OptionalHeaderField.SIZE_OF_STACK_RESERVE, 72, 4),
      value(OptionalHeaderField.SIZE_OF_STACK_COMMIT, 76, 4),
      value(OptionalHeaderField.SIZE_OF_HEAP_RESERVE, 80, 4),
      value(OptionalHeaderField.SIZE_OF_HEAP_COMMIT, 84, 4),
      value(OptionalHeaderField.LOADER_FLAGS, 88, 4),
      value(OptionalHeaderField.NUMBER_OF_RVA_AND_SIZES, 92, 4),
      value(OptionalHeaderField.EXPORT_TABLE, 96, 8),
      value(OptionalHeaderField.IMPORT_TABLE, 104, 8),
      value(OptionalHeaderField.RESOURCE_TABLE, 112, 8),
      value(OptionalHeaderField.EXCEPTION_TABLE, 120, 8),
      value(OptionalHeaderField.CERTIFICATE_TABLE, 128, 8),
      value(OptionalHeaderField.BASE_RELOCATION_TABLE, 136, 8),
      value(OptionalHeaderField.DEBUG_DATA, 144, 8),
      value(OptionalHeaderField.ARCHITECTURE, 152, 8),
      value(OptionalHeaderField.GLOBAL_POINTER, 160, 8),
      value(OptionalHeaderField.THREAD_LOCAL_STORAGE_TABLE, 168, 8),
      value(OptionalHeaderField.LOAD_CONFIGURATION_TABLE, 176, 8),
      value(OptionalHeaderField.BOUND_IMPORT_TABLE, 184, 8),
      value(OptionalHeaderField.IMPORT_ADDRESS_TABLE, 192, 8),
      value(OptionalHeaderField.DELAY_IMPORT_DESCRIPTOR, 200, 8),
      value(OptionalHeaderField.CLR_RUNTIME_HEADER, 208, 8),
      value(OptionalHeaderField.RESERVED, 216, 8)
  );
  // @formatter:on

  /**
   * @param buffer
   */
  public OptionalHeaderStd(ByteOrderBuffer buffer) {
    parseAndLoadOptionalHeader(buffer);
  }

  /**
   * 
   */
  @Override
  protected FieldData dataForField(OptionalHeaderField field) {
    return headerData.get(field);
  }

}
