// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable;

/**
 * 
 */
public class Constants {
  /**
   * Private constructor - must use the defined constants.
   */
  private Constants() {}

  public static final String UTC_TIME_ZONE = "UTC";
  public static final int PE_SIGNATURE_LOCATION = 60;
  public static final byte[] PE_SIGNATURE = {'P', 'E', 0, 0};
  public static final int PE_HEADER_SIZE = 20;
  public static final int PE_SECTION_SIZE = 40;
}
