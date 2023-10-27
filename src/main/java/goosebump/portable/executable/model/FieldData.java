// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import lombok.Value;

/**
 * 
 */
@Value
public class PEFieldData {
  private Enum<?> field;
  private int offset;
  private int size;

  public static Entry<Enum<?>, PEFieldData> value(Enum<?> field, int offset, int size) {
    return new SimpleEntry<Enum<?>, PEFieldData>(field, new PEFieldData(field, offset, size));
  }
}
