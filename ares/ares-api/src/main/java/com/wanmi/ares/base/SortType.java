
package com.wanmi.ares.base;


public enum SortType {
  ASC(0),
  DESC(1);

  private final int value;

  private SortType(int value) {
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
  public static SortType findByValue(int value) { 
    switch (value) {
      case 0:
        return ASC;
      case 1:
        return DESC;
      default:
        return null;
    }
  }
}
