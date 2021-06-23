package com.wanmi.sbc.customer.api.provider.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.quicklogin.WeChatQuickLoginAddReq;
import com.wanmi.sbc.customer.bean.vo.WeChatQuickLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 16:17
 **/
@FeignClient(value = "${application.customer.name}", contextId = "WeChatQuickLoginProvider")
public interface WeChatQuickLoginProvider {

    @PostMapping("/customer/${application.customer.version}/third/login/save")
    BaseResponse<WeChatQuickLoginVo> save(@RequestBody @Valid WeChatQuickLoginAddReq req);
}
