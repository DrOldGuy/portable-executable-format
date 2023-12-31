// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import lombok.Value;

/**
 * This class stores an ordinal value along with a name for the exports table.
 */
@Value
public class Export implements Comparable<Export> {
  private String name;
  private int ordinal;

  /**
   * 
   * @param that
   * @return
   */
  @Override
  public int compareTo(Export that) {
    return this.name.compareTo(that.name);
  }
}
