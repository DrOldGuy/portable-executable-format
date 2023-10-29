// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model;

import lombok.Builder;
import lombok.Value;

/**
 * 
 */
@Value
@Builder
public class DirectoryTable {
  private Directory exports;
  private Directory imports;
  private Directory resources;
  private Directory exceptions;
  private Directory certificates;
  private Directory relocations;
  private Directory debugData;
  private Directory architecture;
  private Directory globalPointer;
  private Directory threadLocalStorage;
  private Directory loadConfiguration;
  private Directory boundImport;
  private Directory importAddress;
  private Directory delayImportDescriptor;
  private Directory clrRuntimeHeader;
  private Directory reserved;
}
