// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

/**
 * These are the fields in a PE data section.
 */
public enum SectionField {
  // @formatter:off
  NAME,
  VIRTUAL_SIZE,
  VIRTUAL_ADDRESS,
  SIZE_OF_RAW_DATA,
  POINTER_TO_RAW_DATA,
  POINTER_TO_RELOCATIONS,
  POINTER_TO_LINE_NUMBERS,
  NUMBER_OF_RELOCATIONS,
  NUMBER_OF_LINE_NUMBERS,
  CHARACTERISTICS
  // @formatter:on
}
