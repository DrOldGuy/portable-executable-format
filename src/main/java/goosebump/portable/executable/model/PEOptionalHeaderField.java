// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

/**
 * 
 */
public enum PEOptionalHeaderField {
  // @formatter:off
  MAGIC_NUMBER,
  MAJOR_LINKER_VERSION,
  MINOR_LINKER_VERSION,
  SIZE_OF_CODE,
  SIZE_OF_INITIALIZED_DATA,
  SIZE_OF_UNINITIALIZED_DATA,
  ADDRESS_OF_ENTRY_POINT,
  BASE_OF_CODE,
  BASE_OF_DATA,
  IMAGE_BASE,
  SECTION_ALIGNMENT,
  FILE_ALIGNMENT,
  MAJOR_OPERATING_SYSTEM_VERSION,
  MINOR_OPERATING_SYSTEM_VERSION,
  MAJOR_IMAGE_VERSION,
  MINOR_IMAGE_VERSION,
  MAJOR_SUBSYSTEM_VERSION,
  MINOR_SUBSYSTEM_VERSION,
  WIN32_VERSION_VALUE,
  SIZE_OF_IMAGE,
  SIZE_OF_HEADERS,
  CHECKSUM,
  SUBSYSTEM,
  DLL_CHARACTERISTICS,
  SIZE_OF_STACK_RESERVE,
  SIZE_OF_STACK_COMMIT,
  SIZE_OF_HEAP_RESERVE,
  SIZE_OF_HEAP_COMMIT,
  LOADER_FLAGS,
  NUMBER_OF_RVA_AND_SIZES,
  
  EXPORT_TABLE,
  IMPORT_TABLE,
  RESOURCE_TABLE,
  EXCEPTION_TABLE,
  CERTIFICATE_TABLE,
  BASE_RELOCATION_TABLE,
  DEBUG_DATA,
  ARCHITECTURE,
  GLOBAL_POINTER,
  THREAD_LOCAL_STORAGE_TABLE,
  LOAD_CONFIGURATION_TABLE,
  BOUND_IMPORT_TABLE,
  IMPORT_ADDRESS_TABLE,
  DELAY_IMPORT_DESCRIPTOR,
  CLR_RUNTIME_HEADER,
  RESERVED,
  // @formatter:on
}
