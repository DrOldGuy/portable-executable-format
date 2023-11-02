// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

/**
 * These define field names in the main PE header.
 */
public enum HeaderField {
  MACHINE,
  NUMBER_OF_SECTIONS,
  TIME_DATE_STAMP,
  POINTER_TO_SYMBOL_TABLE,
  NUMBER_OF_SYMBOLS,
  SIZE_OF_OPTIONAL_HEADER,
  CHARACTERISTICS
}
