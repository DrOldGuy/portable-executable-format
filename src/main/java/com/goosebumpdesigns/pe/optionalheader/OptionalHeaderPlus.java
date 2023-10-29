// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.optionalheader;

import static com.goosebumpdesigns.pe.model.type.FieldData.value;
import java.util.Map;
import com.goosebumpdesigns.pe.PEOptionalHeader;
import com.goosebumpdesigns.pe.file.ByteOrderBuffer;
import com.goosebumpdesigns.pe.model.type.FieldData;
import com.goosebumpdesigns.pe.model.type.OptionalHeaderField;

/**
 * 
 */
public class OptionalHeaderPlus extends PEOptionalHeader {
  // @formatter:off
  private Map<Enum<?>, FieldData> data = Map.ofEntries(
      value(OptionalHeaderField.MAGIC_NUMBER, 0,2),
      value(OptionalHeaderField.MAJOR_LINKER_VERSION, 2, 1),
      value(OptionalHeaderField.MINOR_LINKER_VERSION, 3, 1),
      value(OptionalHeaderField.SIZE_OF_CODE, 4, 4),
      value(OptionalHeaderField.SIZE_OF_INITIALIZED_DATA, 8, 4),
      value(OptionalHeaderField.SIZE_OF_UNINITIALIZED_DATA, 12, 4),
      value(OptionalHeaderField.ADDRESS_OF_ENTRY_POINT, 16, 4),
      value(OptionalHeaderField.BASE_OF_CODE, 20, 4),
      value(OptionalHeaderField.IMAGE_BASE, 24, 8),
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
      value(OptionalHeaderField.SIZE_OF_STACK_RESERVE, 72, 8),
      value(OptionalHeaderField.SIZE_OF_STACK_COMMIT, 80, 8),
      value(OptionalHeaderField.SIZE_OF_HEAP_RESERVE, 88, 8),
      value(OptionalHeaderField.SIZE_OF_HEAP_COMMIT, 96, 8),
      value(OptionalHeaderField.LOADER_FLAGS, 104, 4),
      value(OptionalHeaderField.NUMBER_OF_RVA_AND_SIZES, 108, 4),
      value(OptionalHeaderField.EXPORT_TABLE, 112, 8),
      value(OptionalHeaderField.IMPORT_TABLE, 120, 8),
      value(OptionalHeaderField.RESOURCE_TABLE, 128, 8),
      value(OptionalHeaderField.EXCEPTION_TABLE, 136, 8),
      value(OptionalHeaderField.CERTIFICATE_TABLE, 144, 8),
      value(OptionalHeaderField.BASE_RELOCATION_TABLE, 152, 8),
      value(OptionalHeaderField.DEBUG_DATA, 160, 8),
      value(OptionalHeaderField.ARCHITECTURE, 168, 8),
      value(OptionalHeaderField.GLOBAL_POINTER, 176, 8),
      value(OptionalHeaderField.THREAD_LOCAL_STORAGE_TABLE, 184, 8),
      value(OptionalHeaderField.LOAD_CONFIGURATION_TABLE, 192, 8),
      value(OptionalHeaderField.BOUND_IMPORT_TABLE, 200, 8),
      value(OptionalHeaderField.IMPORT_ADDRESS_TABLE, 208, 8),
      value(OptionalHeaderField.DELAY_IMPORT_DESCRIPTOR, 216, 8),
      value(OptionalHeaderField.CLR_RUNTIME_HEADER, 224, 8),
      value(OptionalHeaderField.RESERVED, 232, 8)
  );
  // @formatter:on

  /**
   * This constructor must call the data loader method because if it is called from the superclass
   * constructor, it is executed <em>before</em> the dataHeader map is initialized.
   * 
   * @param buffer
   */
  public OptionalHeaderPlus(ByteOrderBuffer buffer) {
    parseAndLoadOptionalHeader(buffer);
  }

  /**
   * 
   */
  @Override
  protected FieldData dataForField(OptionalHeaderField field) {
    return data.get(field);
  }

}
