package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.EmailConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.EmailConfigQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "EmailConfigProvider")
public interface EmailConfigProvider {

    /**
     * 查询BOSS管理后台邮箱接口配置
     */
    @PostMapping("/setting/${application.setting.version}/email/query-email-config")
    BaseResponse<EmailConfigQueryResponse> queryEmailConfig();

    /**
     * 更新邮箱接口配置
     *
     * @param request {@link EmailConfigModifyRequest}
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/email/modify-email-config")
    BaseResponse<EmailConfigQueryResponse> modifyEmailConfig(@RequestBody EmailConfigModifyRequest request);

}
