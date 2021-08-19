package com.wanmi.sbc.customer.api.provider.print;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.print.PrintSettingByStoreIdRequest;
import com.wanmi.sbc.customer.api.response.print.PrintSettingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 打印设置-打印设置查询API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "PrintSettingQueryProvider")
public interface PrintSettingQueryProvider {


    /**
     * 根据店铺Id查询打印设置
     *
     * @param request 包含id的查询参数 {@link PrintSettingByStoreIdRequest}
     * @return 打印设置 {@link PrintSettingResponse}
     */
    @PostMapping("/customer/${application.customer.version}/print-setting/get-print-setting-by-store-id")
    BaseResponse<PrintSettingResponse> getPrintSettingByStoreId(@RequestBody @Valid PrintSettingByStoreIdRequest
                                                                        request);
}
