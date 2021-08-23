
package com.wanmi.ares.request.customer;


/**
 * 查询类别
 */
public enum CustomerQueryType  {
  CUSTOMER(0),
  CUSTOMER_LEVEL(1),
  CUSTOMER_AREA(2);

  private final int value;

  private CustomerQueryType(int value) {
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
  public static CustomerQueryType findByValue(int value) { 
    switch (value) {
      case 0:
        return CUSTOMER;
      case 1:
        return CUSTOMER_LEVEL;
      case 2:
        return CUSTOMER_AREA;
      default:
        return null;
    }
  }
}
