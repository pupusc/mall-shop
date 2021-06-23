
package com.wanmi.ares.interfaces.employee;

public class EmployeeQueryService {

  /**
   * 业务员报表查询
   */
  public interface Iface {

    /**
     * 查询业务员获客报表
     *
     * @param request
     */
    public com.wanmi.ares.view.employee.EmployeeClientResponse queryViewByClient(com.wanmi.ares.request.employee.EmployeeClientQueryRequest request);

    /**
     * 查询业务员业绩报表
     *
     * @param request
     */
    public com.wanmi.ares.view.employee.EmployeePerormanceResponse queryViewByPerformance(com.wanmi.ares.request.employee.EmployeePerformanceQueryRequest request);

  }
}