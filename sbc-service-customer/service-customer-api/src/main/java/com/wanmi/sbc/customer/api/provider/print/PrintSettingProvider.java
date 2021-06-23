package com.wanmi.sbc.customer.api.provider.print;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.print.PrintSettingSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 打印设置-打印设置添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/12 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "PrintSettingProvider")
public interface PrintSettingProvider {


    /**
     * 修改打印设置
     *
     * @param request 修改公司信息request{@link PrintSettingSaveRequest}
     * @return 修改打印设置结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/print-setting/modify-print-setting")
    BaseResponse modifyPrintSetting(@RequestBody @Valid PrintSettingSaveRequest request);
}
