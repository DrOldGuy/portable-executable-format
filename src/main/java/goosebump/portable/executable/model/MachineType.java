// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model;

import java.nio.ByteBuffer;

/**
 * This enum represents the machine type of a compiled DLL. This is taken from:
 * <a href="https://learn.microsoft.com/en-us/windows/win32/debug/pe-format#machine-types">Windows
 * documentation</a>.
 */
public enum MachineType {
  // @formatter:off
  IMAGE_FILE_MACHINE_UNKNOWN(0x0000),
  IMAGE_FILE_MACHINE_ALPHA(0x0184),
  IMAGE_FILE_MACHINE_ALPHA64(0x0284),
  IMAGE_FILE_MACHINE_AM33(0x01d3),
  IMAGE_FILE_MACHINE_AMD64(0x8664),
  IMAGE_FILE_MACHINE_ARM(0x01c0),
  IMAGE_FILE_MACHINE_ARM64(0xaa64),
  IMAGE_FILE_MACHINE_ARMNT(0x01c4),
  IMAGE_FILE_MACHINE_AXP64(0x0284),
  IMAGE_FILE_MACHINE_EBC(0xebc),
  IMAGE_FILE_MACHINE_I386(0x014c),
  IMAGE_FILE_MACHINE_IA64(0x0200),
  IMAGE_FILE_MACHINE_LOONGARCH32(0x6232),
  IMAGE_FILE_MACHINE_LOONGARCH64(0x6264),
  IMAGE_FILE_MACHINE_M32R(0x9041),
  IMAGE_FILE_MACHINE_MIPS16(0x0266),
  IMAGE_FILE_MACHINE_MIPSFPU(0x0366),
  IMAGE_FILE_MACHINE_MIPSFPU16(0x0466),
  IMAGE_FILE_MACHINE_POWERPC(0x01f0),
  IMAGE_FILE_MACHINE_POWERPCFP(0x01f1),
  IMAGE_FILE_MACHINE_R4000(0x0166),
  IMAGE_FILE_MACHINE_RISCV32(0x5032),
  IMAGE_FILE_MACHINE_RISCV64(0x5064),
  IMAGE_FILE_MACHINE_RISCV128(0x5128),
  IMAGE_FILE_MACHINE_SH3(0x01a2),
  IMAGE_FILE_MACHINE_SH3DSP(0x01a3),
  IMAGE_FILE_MACHINE_SH4(0x01a6),
  IMAGE_FILE_MACHINE_SH5(0x01a8),
  IMAGE_FILE_MACHINE_THUMB(0x01c2),
  IMAGE_FILE_MACHINE_WCEMIPSV2(0x0169);
  // @formatter:on

  private short value;

  /**
   * Private constructor takes the value.
   * 
   * @param value
   */
  private MachineType(int value) {
    byte[] bytes = {(byte)((value >> 8) & 0x000000ff), (byte)(value & 0x000000ff)};
    ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);

    buffer.put(bytes);
    buffer.rewind();
    this.value = buffer.getShort();
  }

  /**
   * Return the machine type given the value.
   * 
   * @param value
   * @return
   */
  public static MachineType valueOf(int value) {
    short shValue = (short)value;

    for (MachineType machineType : MachineType.values()) {
      if (shValue == machineType.value) {
        return machineType;
      }
    }

    return IMAGE_FILE_MACHINE_UNKNOWN;
  }

  /**
   * Returns the value associated with the machine type object.
   * 
   * @return
   */
  public int value() {
    return value;
  }
}
