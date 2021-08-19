
package com.wanmi.ares.enums;


/**
 * 业务员获客报表排序规则
 * 
 */
public enum EmployeePerformanceSort {
  /**
   * 按下单笔数
   * 
   */
  ORDER_COUNT_ASC(0),
  ORDER_COUNT_DESC(1),
  /**
   * 按下单人数
   * 
   */
  ORDER_CUSTOMER_ASC(2),
  ORDER_CUSTOMER_DESC(3),
  /**
   * 按下单金额
   * 
   */
  ORDER_AMT_ASC(4),
  ORDER_AMT_DESC(5),
  /**
   * 按付款笔数
   * 
   */
  PAY_COUNT_ASC(6),
  PAY_COUNT_DESC(7),
  /**
   * 按付款人数
   * 
   */
  PAY_CUSTOMER_ASC(8),
  PAY_CUSTOMER_DESC(9),
  /**
   * 按付款金额
   * 
   */
  PAY_AMT_ASC(10),
  PAY_AMT_DESC(11),
  /**
   * 按退单笔数
   * 
   */
  RETURN_COUNT_ASC(12),
  RETURN_COUNT_DESC(13),
  /**
   * 按退单人数
   * 
   */
  RETURN_CUSTOMER_ASC(14),
  RETURN_CUSTOMER_DESC(15),
  /**
   * 按退单金额
   * 
   */
  RETURN_AMT_ASC(16),
  RETURN_AMT_DESC(17),
  /**
   * 按客单价
   * 
   */
  CUSTOMER_UNIT_PRICE_ASC(18),
  CUSTOMER_UNIT_PRICE_DESC(19),
  /**
   * 按笔单价
   * 
   */
  ORDER_UNIT_PRICE_ASC(20),
  ORDER_UNIT_PRICE_DESC(21);

  private final int value;

  private EmployeePerformanceSort(int value) {
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
  public static EmployeePerformanceSort findByValue(int value) { 
    switch (value) {
      case 0:
        return ORDER_COUNT_ASC;
      case 1:
        return ORDER_COUNT_DESC;
      case 2:
        return ORDER_CUSTOMER_ASC;
      case 3:
        return ORDER_CUSTOMER_DESC;
      case 4:
        return ORDER_AMT_ASC;
      case 5:
        return ORDER_AMT_DESC;
      case 6:
        return PAY_COUNT_ASC;
      case 7:
        return PAY_COUNT_DESC;
      case 8:
        return PAY_CUSTOMER_ASC;
      case 9:
        return PAY_CUSTOMER_DESC;
      case 10:
        return PAY_AMT_ASC;
      case 11:
        return PAY_AMT_DESC;
      case 12:
        return RETURN_COUNT_ASC;
      case 13:
        return RETURN_COUNT_DESC;
      case 14:
        return RETURN_CUSTOMER_ASC;
      case 15:
        return RETURN_CUSTOMER_DESC;
      case 16:
        return RETURN_AMT_ASC;
      case 17:
        return RETURN_AMT_DESC;
      case 18:
        return CUSTOMER_UNIT_PRICE_ASC;
      case 19:
        return CUSTOMER_UNIT_PRICE_DESC;
      case 20:
        return ORDER_UNIT_PRICE_ASC;
      case 21:
        return ORDER_UNIT_PRICE_DESC;
      default:
        return null;
    }
  }
}
