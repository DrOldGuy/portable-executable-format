// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe.model.type;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public enum SectionCharacteristic {
  // @formatter:off
  
  IMAGE_SCN_TYPE_NO_PAD(0x00000008),
  IMAGE_SCN_CNT_CODE(0x00000020),
  IMAGE_SCN_CNT_INITIALIZED_DATA(0x00000040),
  IMAGE_SCN_CNT_UNINITIALIZED_DATA(0x00000080),
  IMAGE_SCN_LNK_OTHER(0x00000100),
  IMAGE_SCN_LNK_INFO(0x00000200),
  IMAGE_SCN_LNK_REMOVE(0x00000800),
  IMAGE_SCN_LNK_COMDAT(0x00001000),
  IMAGE_SCN_GPREL(0x00008000),
  IMAGE_SCN_MEM_PURGEABLE(0x00020000),
  IMAGE_SCN_MEM_16BIT(0x00020000),
  IMAGE_SCN_MEM_LOCKED(0x00040000),
  IMAGE_SCN_MEM_PRELOAD(0x00080000),
  IMAGE_SCN_ALIGN_1BYTES(0x00100000),
  IMAGE_SCN_ALIGN_2BYTES(0x00200000),
  IMAGE_SCN_ALIGN_4BYTES(0x00300000),
  IMAGE_SCN_ALIGN_8BYTES(0x00400000),
  IMAGE_SCN_ALIGN_16BYTES(0x00500000),
  IMAGE_SCN_ALIGN_32BYTES(0x00600000),
  IMAGE_SCN_ALIGN_64BYTES(0x00700000),
  IMAGE_SCN_ALIGN_128BYTES(0x00800000),
  IMAGE_SCN_ALIGN_256BYTES(0x00900000),
  IMAGE_SCN_ALIGN_512BYTES(0x00A00000),
  IMAGE_SCN_ALIGN_1024BYTES(0x00B00000),
  IMAGE_SCN_ALIGN_2048BYTES(0x00C00000),
  IMAGE_SCN_ALIGN_4096BYTES(0x00D00000),
  IMAGE_SCN_ALIGN_8192BYTES(0x00E00000),
  IMAGE_SCN_LNK_NRELOC_OVFL(0x01000000),
  IMAGE_SCN_MEM_DISCARDABLE(0x02000000),
  IMAGE_SCN_MEM_NOT_CACHED(0x04000000),
  IMAGE_SCN_MEM_NOT_PAGED(0x08000000),
  IMAGE_SCN_MEM_SHARED(0x10000000),
  IMAGE_SCN_MEM_EXECUTE(0x20000000),
  IMAGE_SCN_MEM_READ(0x40000000),
  IMAGE_SCN_MEM_WRITE(0x80000000);
  // @formatter:on

  int flag;

  /**
   * Private constructor.
   * 
   * @param value
   */
  private SectionCharacteristic(int value) {
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
  public static List<SectionCharacteristic> allCharacteristicsIn(int flag) {
    List<SectionCharacteristic> characteristics = new LinkedList<>();

    for (SectionCharacteristic characteristic : SectionCharacteristic.values()) {
      if (characteristic.isPresentIn(flag)) {
        characteristics.add(characteristic);
      }
    }

    return characteristics;
  }
}
