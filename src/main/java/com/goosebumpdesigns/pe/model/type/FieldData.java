// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import lombok.Value;

/**
 * This class contains data for a field name, offset and length in a Personal Executable header.
 * There is also a method that returns an {@link Entry} object that can be loaded directly into a
 * Map.
 */
@Value
public class FieldData {
  private Enum<?> field;
  private int offset;
  private int size;

  /**
   * Used to load field information into a field map.
   * 
   * @param field The field name (enum value).
   * @param offset The offset in the header (or structure).
   * @param size The length of the field in bytes.
   * @return An {@link Entry} object that can be loaded directly into a Map (Map.ofEntries()).
   */
  public static Entry<Enum<?>, FieldData> value(Enum<?> field, int offset, int size) {
    return new SimpleEntry<Enum<?>, FieldData>(field, new FieldData(field, offset, size));
  }
}
