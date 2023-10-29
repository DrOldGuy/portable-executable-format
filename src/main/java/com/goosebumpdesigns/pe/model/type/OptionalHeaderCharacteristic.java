// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public enum OptionalHeaderCharacteristic {
  // @formatter:off
  IMAGE_DLLCHARACTERISTICS_HIGH_ENTROPY_VA(0x0020),
  IMAGE_DLLCHARACTERISTICS_DYNAMIC_BASE(0x0040),
  IMAGE_DLLCHARACTERISTICS_FORCE_INTEGRITY(0x0080),
  IMAGE_DLLCHARACTERISTICS_NX_COMPAT(0x0100),
  IMAGE_DLLCHARACTERISTICS_NO_ISONATION(0x0200),
  IMAGE_DLLCHARACTERISTICS_NO_SEH(0x0400),
  IMAGE_DLLCHARACTERISTICS_NO_BIND(0x0800),
  IMAGE_DLLCHARACTERISTICS_APP_CONTAINER(0x1000),
  IMAGE_DLLCHARACTERISTICS_WDM_DRIVER(0x2000),
  IMAGE_DLLCHARACTERISTICS_GUARD_CF(0x4000),
  IMAGE_DLLCHARACTERISTICS_TERMINAL_SERVER_AWARE(0x8000);
  // @formatter:on

  int flag;

  /**
   * Private constructor.
   * 
   * @param value
   */
  private OptionalHeaderCharacteristic(int value) {
    this.flag = value;
  }

  /**
   * Returns the value of the characteristic.
   * 
   * @return The value
   */
  public int value() {
    return flag;
  }

  /**
   * Returns {@code true} if the given flag value has the characteristic. Usage:
   * 
   * <pre>
   * int flag = getFlagSomehow();
   * boolean hasFileSystem = IMAGE_FILE_SYSTEM.isPresentIn(flag);
   * </pre>
   * 
   * @param flag
   * @return
   */
  public boolean isPresentIn(int flag) {
    return (flag & this.flag) != 0;
  }

  /**
   * Returns a list of characteristics present in the given flag.
   * 
   * @param flag The flag to test
   * @return A list of present characteristics
   */
  public static List<OptionalHeaderCharacteristic> allCharacteristicsIn(int flag) {
    List<OptionalHeaderCharacteristic> characteristics = new LinkedList<>();

    for (OptionalHeaderCharacteristic characteristic : OptionalHeaderCharacteristic.values()) {
      if (characteristic.isPresentIn(flag)) {
        characteristics.add(characteristic);
      }
    }

    return characteristics;
  }
}
