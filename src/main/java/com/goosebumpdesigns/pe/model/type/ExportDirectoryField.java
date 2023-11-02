// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

/**
 * These define the fields found in the export directory of the PE file.
 */
public enum ExportDirectoryField {
  // @formatter:off
  EXPORT_FLAGS,
  TIMESTAMP,
  MAJOR_VERSION,
  MINOR_VERSION,
  NAME_RVA,
  ORDINAL_BASE,
  NUMBER_OF_ADDRESS_TABLE_ENTRIES,
  NUMBER_OF_NAME_POINTERS,
  EXPORT_ADDRESS_TABLE_RVA,
  NAME_POINTER_RVA,
  ORDINAL_TABLE_RVA,
  // @formatter:on
}
