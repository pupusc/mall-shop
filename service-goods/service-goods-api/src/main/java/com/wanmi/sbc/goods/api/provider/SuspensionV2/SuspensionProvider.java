package com.wanmi.sbc.goods.api.provider.SuspensionV2;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByIdRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>悬浮窗查询服务Provider</p>
 * @author lws
 * @date 2023-02-4
 */
@FeignClient(value = "${application.goods.name}", contextId = "SuspensionProvider")
public interface SuspensionProvider {
    /**
     * 分页查询自动标签API
     * @param   suspensionByIdRequest
     * @return 悬浮窗信息 {@link suspensionByIdRequest}
     */
    @PostMapping("/goods/suspension/get-by-id")
    BaseResponse<SuspensionByIdResponse> getById(@RequestBody @Valid SuspensionByIdRequest suspensionByIdRequest);

}
