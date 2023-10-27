// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import lombok.Value;

/**
 * 
 */
@Value
public class PEDirectory {
  private long virtualAddress;
  private long size;
}
