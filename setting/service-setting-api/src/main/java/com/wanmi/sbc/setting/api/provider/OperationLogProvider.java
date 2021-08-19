package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.OperationLogAddRequest;
import com.wanmi.sbc.setting.api.response.OperationLogAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "OperationLogProvider")
public interface OperationLogProvider {

    /**
     * 新增操作日志
     *
     * @param request {@link OperationLogAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/operation-log/add")
    BaseResponse<OperationLogAddResponse> add(@RequestBody OperationLogAddRequest request);
}
