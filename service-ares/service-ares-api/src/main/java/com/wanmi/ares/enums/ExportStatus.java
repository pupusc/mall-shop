
package com.wanmi.ares.enums;


/**
 * 导出状态
 */
public enum ExportStatus  {
  WAIT_EXPORT(0),
  SUCCESS_EXPORT(1),
  ERROR_EXPORT(2);

  private final int value;

  private ExportStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ExportStatus findByValue(int value) { 
    switch (value) {
      case 0:
        return WAIT_EXPORT;
      case 1:
        return SUCCESS_EXPORT;
      case 2:
        return ERROR_EXPORT;
      default:
        return null;
    }
  }
}
