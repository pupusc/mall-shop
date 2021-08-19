
package com.wanmi.ares.enums;



/**
 * 按日期周期查询参数枚举
 */
public enum QueryDateCycle {
  /**
   * 今天
   * 
   */
  today(0),
  /**
   * 昨天
   * 
   */
  yesterday(1),
  /**
   * 近7天
   * 
   */
  latest7Days(2),
  /**
   * 最近30天
   * 
   */
  latest30Days(3),
  /**
   * 最近10天
   */
  latest10Days(4);

  private final int value;

  private QueryDateCycle(int value) {
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
  public static QueryDateCycle findByValue(int value) { 
    switch (value) {
      case 0:
        return today;
      case 1:
        return yesterday;
      case 2:
        return latest7Days;
      case 3:
        return latest30Days;
      case 4:
        return latest10Days;
      default:
        return null;
    }
  }
}
