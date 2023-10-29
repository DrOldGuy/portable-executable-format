// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import lombok.Value;

/**
 * 
 */
@Value
public class FieldData {
  private Enum<?> field;
  private int offset;
  private int size;

  public static Entry<Enum<?>, FieldData> value(Enum<?> field, int offset, int size) {
    return new SimpleEntry<Enum<?>, FieldData>(field, new FieldData(field, offset, size));
  }
}