// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import lombok.Value;

/**
 * This class contains a major and minor version number.
 */
@Value
public class Version {
  private int major;
  private int minor;
}
