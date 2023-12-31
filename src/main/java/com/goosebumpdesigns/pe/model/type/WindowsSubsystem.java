// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

/**
 * This class defines the Windows subsystem values used in the Personal Executable optional header.
 */
public enum WindowsSubsystem {
  // @formatter:off
  IMAGE_SUBSYSTEM_UNKNOWN(0),
  IMAGE_SUBSYSTEM_NATIVE(1),
  IMAGE_SUBSYSTEM_WINDOWS_GUI(2),
  IMAGE_SUBSYSTEM_WINDOWS_CUI(3),
  IMAGE_SUBSYSTEM_O2S_CUI(5),
  IMAGE_SUBSYSTEM_POSIX_CUI(7),
  IMAGE_SUBSYSTEM_NATIVE_WINDOWS(8),
  IMAGE_SUBSYSTEM_WINDOWS_CE_GUI(9),
  IMAGE_SUBSYSTEM_EFI_APPLICATION(10),
  IMAGE_SUBSYSTEM_EFI_BOOT_SERVICE_DRIVER(11),
  IMAGE_SUBSYSTEM_EFI_RUNTIME_DRIVER(12),
  IMAGE_SUBSYSTEM_EFI_ROM(13),
  IMAGE_SUBSYSTEM_XBOX(14),
  IMAGE_SUBSYSTEM_WINDOWS_BOOT_APPLICATION(16);
  
  // @formatter:on

  private int value;

  private WindowsSubsystem(int value) {
    this.value = value;
  }

  /**
   * Find the subsystem given the value.
   * 
   * @param value
   * @return
   */
  public static WindowsSubsystem valueOf(int value) {
    for (WindowsSubsystem subsystem : WindowsSubsystem.values()) {
      if (subsystem.value == value) {
        return subsystem;
      }
    }

    return IMAGE_SUBSYSTEM_UNKNOWN;
  }

  /**
   * Returnt the value of a subsystem element.
   * 
   * @return
   */
  public int value() {
    return value;
  }
}
