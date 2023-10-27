// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import lombok.Builder;
import lombok.Value;

/**
 * 
 */
@Value
@Builder
public class PEDirectoryTable {
  private PEDirectory exports;
  private PEDirectory imports;
  private PEDirectory resources;
  private PEDirectory exceptions;
  private PEDirectory certificates;
  private PEDirectory relocations;
  private PEDirectory debugData;
  private PEDirectory architecture;
  private PEDirectory globalPointer;
  private PEDirectory threadLocalStorage;
  private PEDirectory loadConfiguration;
  private PEDirectory boundImport;
  private PEDirectory importAddress;
  private PEDirectory delayImportDescriptor;
  private PEDirectory clrRuntimeHeader;
  private PEDirectory reserved;
}
