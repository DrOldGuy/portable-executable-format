// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import lombok.Value;

/**
 * This class represents a directory address (offset into the file) and a size for a resource.
 */
@Value
public class Directory {
  private long virtualAddress;
  private long size;
}
