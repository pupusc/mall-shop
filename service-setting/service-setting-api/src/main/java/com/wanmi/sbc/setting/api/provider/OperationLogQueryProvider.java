package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.api.response.OperationLogListResponse;
import com.wanmi.sbc.setting.api.response.OperationLogPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "OperationLogQueryProvider")
public interface OperationLogQueryProvider {
    /**
     * 查询操作日志
     *
     * @param queryRequest {@link OperationLogListRequest}
     * @return 操作日志列表 {@link OperationLogListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/operation-log/list")
    BaseResponse<OperationLogListResponse> list(@RequestBody OperationLogListRequest queryRequest);

    /**
     * 根据条件查询操作日志
     *
     * @param queryRequest {@link OperationLogListRequest}
     * @return {@link OperationLogPageResponse}
     */
    @PostMapping("/setting/operation-log/queryOpLogByCriteria")
    BaseResponse<OperationLogPageResponse> queryOpLogByCriteria(@RequestBody OperationLogListRequest queryRequest);

    /**
     * 根据条件导出操作日志
     *
     * @param queryRequest {@link OperationLogListRequest}
     * @return {@link OperationLogListResponse}
     */
    @PostMapping("/setting/operation-log/exportOpLogByCriteria")
    BaseResponse<OperationLogListResponse> exportOpLogByCriteria(@RequestBody OperationLogListRequest queryRequest);
}
