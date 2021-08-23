
package com.wanmi.ares.enums;


/**
 * 业务员获客报表排序规则
 * 
 */
public enum EmployeeClientSort{
  /**
   * 按客户总数升序
   * 
   */
  TOTAL_ASC(0),
  /**
   * 按客户总数降序
   * 
   */
  TOTAL_DESC(1),
  /**
   * 按新增客户数升序
   * 
   */
  NEWLY_ASC(2),
  /**
   * 按新增客户数降序
   * 
   */
  NEWLY_DESC(3);

  private final int value;

  private EmployeeClientSort(int value) {
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
  public static EmployeeClientSort findByValue(int value) { 
    switch (value) {
      case 0:
        return TOTAL_ASC;
      case 1:
        return TOTAL_DESC;
      case 2:
        return NEWLY_ASC;
      case 3:
        return NEWLY_DESC;
      default:
        return null;
    }
  }
}
