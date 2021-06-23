
package com.wanmi.ares.view.customer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户增长视图
 *
 */
@Data
@Accessors(chain = true)
public class CustomerOrderView {

  public java.lang.String id; // required
  public java.lang.String companyId; // required
  public long orderCount; // required
  public double amount; // required
  public long skuCount; // required
  /**
   * 付款总金额
   */
  public double payAmount; // required
  /**
   * 笔单价
   */
  public double orderPerPrice; // required
  /**
   * 退单总笔数
   */
  public long returnCount; // required
  /**
   * 退单总金额
   */
  public double returnAmount; // required
  /**
   * 退货商品总件数
   */
  public long returnSkuCount; // required
  /**
   * 客户数量字段冗余，用于客单价计算
   */
  public long customerCount; // required
  public java.lang.String levelName; // required
  public double userPerPrice; // required
  public java.lang.String customerName; // required
  public java.lang.String cityId; // required
  public java.lang.String account; // required
  public java.lang.String levelId; // required
  public long payOrderCount; // required
}