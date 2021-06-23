package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.ExportDataServiceProvider;
import com.wanmi.ares.request.export.ExportDataRequest;
import com.wanmi.ares.view.export.ExportDataResponse;
import com.wanmi.ares.view.export.ExportDataView;
import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.configure.ThriftClientConfig;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导出任务要求控制器
 * Author: bail
 * Time: 2017/11/2.16:16
 */
@Api(tags = "ExportDataController", description = "导出任务要求 Api")
@RestController
@RequestMapping("/export")
@Slf4j
@EnableConfigurationProperties(ThriftClientConfig.class)
public class ExportDataController {
    @Autowired
    private ExportDataServiceProvider exportDataServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 发送导出任务请求
     *
     * @param expRequest
     * @return
     */
    @ApiOperation(value = "发送导出任务请求")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public BaseResponse<ExportDataView> send(@RequestBody ExportDataRequest expRequest) {
        try {
            String employeeId = commonUtil.getOperatorId();
            expRequest.setUserId(employeeId);
            ExportDataView response = exportDataServiceProvider.sendExportDataRequest(expRequest);
            return BaseResponse.success(response);
        } catch (Exception e) {
            log.error("Get exportData client view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }

    /**
     * 分页查询历史导出任务请求
     *
     * @param expRequest
     * @return
     */
    @ApiOperation(value = "分页查询历史导出任务请求")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BaseQueryResponse<ExportDataView>> query(@RequestBody ExportDataRequest expRequest) {
        try {
            String employeeId = commonUtil.getOperatorId();
            expRequest.setUserId(employeeId);
            ExportDataResponse response = exportDataServiceProvider.queryExportDataRequestPage(expRequest);
            return BaseResponse.success(BaseQueryResponse.<ExportDataView>builder()
                    .total(response.getTotal())
                    .data(response.getViewList())
                    .pageSize(expRequest.getPageSize())
                    .pageNum(expRequest.getPageNum())
                    .build());
        } catch (Exception e) {
            log.error("Get exportData client view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }

    /**
     * 删除某个导出任务请求
     *
     * @param expRequest
     * @return
     */
    @ApiOperation(value = "删除某个导出任务请求")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody ExportDataRequest expRequest) {
        try {
            String employeeId = commonUtil.getOperatorId();
            expRequest.setUserId(employeeId);
            if (exportDataServiceProvider.deleteExportDataRequest(expRequest) > 0) {
                return BaseResponse.SUCCESSFUL();
            } else {
                return new BaseResponse("K-120001");
            }
        } catch (Exception e) {
            log.error("Get exportData client view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }
}
