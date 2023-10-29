// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import lombok.Value;

/**
 * 
 */
@Value
public class Directory {
  private long virtualAddress;
  private long size;
}