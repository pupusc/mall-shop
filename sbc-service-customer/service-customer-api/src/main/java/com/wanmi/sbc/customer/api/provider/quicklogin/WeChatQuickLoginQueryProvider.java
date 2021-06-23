package com.wanmi.sbc.customer.api.provider.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.quicklogin.WeChatQuickLoginQueryReq;
import com.wanmi.sbc.customer.bean.vo.WeChatQuickLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 16:16
 **/
@FeignClient(value = "${application.customer.name}", contextId = "WeChatQuickLoginQueryProvider")
public interface WeChatQuickLoginQueryProvider {

    @PostMapping("/customer/${application.customer.version}/third/login/get")
    BaseResponse<WeChatQuickLoginVo> get(@RequestBody @Valid WeChatQuickLoginQueryReq req);
}
