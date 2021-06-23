package com.wanmi.ares.provider;

import com.wanmi.ares.request.export.ExportDataRequest;
import com.wanmi.ares.view.export.ExportDataResponse;
import com.wanmi.ares.view.export.ExportDataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:26
 */
@FeignClient(name = "${application.ares.name}", contextId = "ExportDataServiceProvider")
public interface ExportDataServiceProvider {

    /**
     * 发送导出报表任务请求
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/exportData/sendExportDataRequest")
    ExportDataView sendExportDataRequest(@RequestBody @Valid ExportDataRequest request);

    /**
     * 分页查询导出任务
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/exportData/queryExportDataRequestPage")
    ExportDataResponse queryExportDataRequestPage(@RequestBody @Valid ExportDataRequest request);

    /**
     * 删除某个导出报表任务
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/exportData/deleteExportDataRequest")
    int deleteExportDataRequest(@RequestBody @Valid ExportDataRequest request);

    /**
     * 删除某个导出报表任务
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/exportData/deleteExportDataRequestAndFiles")
    int deleteExportDataRequestAndFiles(@RequestBody @Valid ExportDataRequest request);

}
