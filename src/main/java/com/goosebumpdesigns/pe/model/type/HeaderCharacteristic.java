// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.model.type;

import java.util.LinkedList;
import java.util.List;

/**
 * These can be combined in the characteristics flag field found in the main PE header.
 */
public enum HeaderCharacteristic {
  // @formatter:off
  IMAGE_FILE_RELOCS_STRIPPED(0x0001),
  IMAGE_FILE_EXECUTABLE_IMAGE(0x0002),
  IMAGE_FILE_LINE_NUMS_STRIPPED(0x0004),
  IMAGE_FILE_LOCAL_SYMS_STRIPPED(0x0008),
  IMAGE_FILE_AGGRESSIVE_WS_TRIM(0x0010),
  IMAGE_FILE_LARGE_ADDRESS_AWARE(0x0020),
  RESERVED(0x0040),
  IMAGE_FILE_BYTES_REVERSED_LO(0x0080),
  IMAGE_FILE_32BIT_MACHINE(0x0100),
  IMAGE_FILE_DEBUG_STRIPPED(0x0200),
  IMAGE_FILE_REMOVABLE_RUN_FROM_SWAP(0x0400),
  IMAGE_FILE_NET_RUN_FROM_SWAP(0x0800),
  IMAGE_FILE_SYSTEM(0x1000),
  IMAGE_FILE_DLL(0x2000),
  IMAGE_FILE_UP_SYSTEM_ONLY(0x4000),
  IMAGE_FILE_BYTES_REVERSED_HI(0x8000);
  // @formatter:on

  int flag;

  private HeaderCharacteristic(int flag) {
    this.flag = flag;
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
  public static List<HeaderCharacteristic> allCharacteristicsIn(int flag) {
    List<HeaderCharacteristic> characteristics = new LinkedList<>();

    for(HeaderCharacteristic ch : HeaderCharacteristic.values()) {
      if(ch.isPresentIn(flag)) {
        characteristics.add(ch);
      }
    }

    return characteristics;
  }
}
