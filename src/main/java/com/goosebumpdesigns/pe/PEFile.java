// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe;

import lombok.Builder;
import lombok.Value;

/**
 * This file is normally populated by {@link PEFileBuilder#build()}. It contains an in-memory of the
 * data in a Portable Executable file (Dynamic Link Library or .exe file).
 * 
 * Note that the Lombok @Value annotation creates an immutable object. The @Builder annotation
 * supplies a builder class that allows this class to be populated. See the
 * <a href="https://projectlombok.org/features/Builder">Lombok documentation</a> for details.
 */
@Value
@Builder
public class PEFile {
  private PEHeader header;
  private PEOptionalHeader optionalHeader;
  private PESectionTable sectionTable;
  private PEExports exports;

}
