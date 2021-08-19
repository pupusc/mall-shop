package com.wanmi.sbc.customer.provider.impl.print;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.print.PrintSettingQueryProvider;
import com.wanmi.sbc.customer.api.request.print.PrintSettingByStoreIdRequest;
import com.wanmi.sbc.customer.api.response.print.PrintSettingResponse;
import com.wanmi.sbc.customer.print.model.root.PrintSetting;
import com.wanmi.sbc.customer.print.service.PrintSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 打印设置-打印设置查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class PrintSettingQueryController implements PrintSettingQueryProvider {

    @Autowired
    private PrintSettingService printSettingService;

    @Override
    public BaseResponse<PrintSettingResponse> getPrintSettingByStoreId(@RequestBody @Valid
                                                                                   PrintSettingByStoreIdRequest
                                                                                   request) {
        PrintSetting printSetting = printSettingService.findByStoreId(request.getStoreId());
        PrintSettingResponse response = new PrintSettingResponse();
        KsBeanUtil.copyPropertiesThird(printSetting, response);
        return BaseResponse.success(response);
    }
}
