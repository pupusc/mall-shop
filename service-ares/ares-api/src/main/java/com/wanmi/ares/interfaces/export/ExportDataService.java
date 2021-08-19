package com.wanmi.ares.interfaces.export;

public class ExportDataService {

  /**
   * 导出报表任务Service
   */
  public interface Iface {

    /**
     * 发送导出报表任务请求
     *
     * @param request
     */
    public com.wanmi.ares.view.export.ExportDataView sendExportDataRequest(com.wanmi.ares.request.export.ExportDataRequest request);

    /**
     * 分页查询导出任务
     *
     * @param request
     */
    public com.wanmi.ares.view.export.ExportDataResponse queryExportDataRequestPage(com.wanmi.ares.request.export.ExportDataRequest request);

    /**
     * 删除某个导出报表任务
     *
     * @param request
     */
    public int deleteExportDataRequest(com.wanmi.ares.request.export.ExportDataRequest request);

    /**
     * 删除某个导出报表任务
     *
     * @param request
     */
    public int deleteExportDataRequestAndFiles(com.wanmi.ares.request.export.ExportDataRequest request);

  }
}