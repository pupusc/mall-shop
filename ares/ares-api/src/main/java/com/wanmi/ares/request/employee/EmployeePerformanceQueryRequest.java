package com.wanmi.ares.request.employee;

import lombok.Data;

/**
 * 业务员获客报表查询参数
 *
 */
@Data
public class EmployeePerformanceQueryRequest {

  /**
   * 搜索关键词
   */
  public String keywords; // optional
  /**
   * 商户id
   */
  public String companyId; // required
  /**
   * 日期周期，如果按年月周期查询，则此项可不必传
   *
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle dataCycle; // optional
  /**
   * 年月周期，格式："yyyyMM",如果按日期周期查询，此项可不必传
   */
  public String yearMonth; // optional
  /**
   * 排序规则
   *
   * @see com.wanmi.ares.enums.EmployeePerformanceSort
   */
  public com.wanmi.ares.enums.EmployeePerformanceSort sort; // required
  /**
   * 当前页码
   */
  public int pageNo; // required
  /**
   * 每页条数
   */
  public int pageSize; // required
  /**
   * 业务员id
   */
  public String employeeId; // optional
}