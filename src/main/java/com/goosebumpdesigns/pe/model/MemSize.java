// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import java.math.BigInteger;
import lombok.Value;

/**
 * This class contains a reserve and commit size. (This means something in the context of Portable
 * Executable file formats.)
 */
@Value
public class MemSize {
  private BigInteger reserve;
  private BigInteger commit;
}
