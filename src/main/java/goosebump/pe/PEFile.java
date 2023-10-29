// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe;

import lombok.Builder;
import lombok.Value;

/**
 * 
 */
@Value
@Builder
public class PEFile {
  private PEHeader header;
  private PEOptionalHeader optionalHeader;
  private PESectionTable sectionTable;
  private PEExports exports;

}
