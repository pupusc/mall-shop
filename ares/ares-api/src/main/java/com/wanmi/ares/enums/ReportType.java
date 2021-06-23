
package com.wanmi.ares.enums;


/**
 * 报表类别
 */
public enum ReportType {
  /**
   * 流量报表
   */
  FLOW(0),
  /**
   * 交易报表
   */
  TRADE(1),
  /**
   * 商品销售报表
   */
  GOODS_TRADE(2),
  /**
   * 商品分类销售报表
   */
  GOODS_CATE_TRADE(3),
  /**
   * 商品品牌销售报表
   */
  GOODS_BRAND_TRADE(4),
  /**
   * 客户增长报表
   */
  CUSTOMER_GROW(5),
  /**
   * 客户交易报表
   */
  CUSTOMER_TRADE(6),
  /**
   * 客户等级交易报表
   */
  CUSTOMER_LEVEL_TRADE(7),
  /**
   * 客户地区交易报表
   */
  CUSTOMER_AREA_TRADE(8),
  /**
   * 业务员业绩报表
   */
  SALESMAN_TRADE(9),
  /**
   * 业务员获客报表
   */
  SALESMAN_CUSTOMER(10),
  /**
   * 商品分类销售报表
   */
  STORE_CATE_TRADE(11),
  /**
   * 店铺流量报表
   */
  STORE_FLOW(12),
  /**
   * 店铺交易报表
   */
  STORE_TRADE(13);

  private final int value;

  private ReportType(int value) {
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
  public static ReportType findByValue(int value) { 
    switch (value) {
      case 0:
        return FLOW;
      case 1:
        return TRADE;
      case 2:
        return GOODS_TRADE;
      case 3:
        return GOODS_CATE_TRADE;
      case 4:
        return GOODS_BRAND_TRADE;
      case 5:
        return CUSTOMER_GROW;
      case 6:
        return CUSTOMER_TRADE;
      case 7:
        return CUSTOMER_LEVEL_TRADE;
      case 8:
        return CUSTOMER_AREA_TRADE;
      case 9:
        return SALESMAN_TRADE;
      case 10:
        return SALESMAN_CUSTOMER;
      case 11:
        return STORE_CATE_TRADE;
      case 12:
        return STORE_FLOW;
      case 13:
        return STORE_TRADE;
      default:
        return null;
    }
  }
}
