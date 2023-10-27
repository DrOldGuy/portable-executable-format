// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import java.math.BigInteger;
import lombok.Value;

/**
 * 
 */
@Value
public class PEMemSize {
  private BigInteger reserve;
  private BigInteger commit;
}
