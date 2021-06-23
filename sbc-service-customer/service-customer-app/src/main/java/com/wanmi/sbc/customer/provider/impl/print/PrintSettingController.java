package com.wanmi.sbc.customer.provider.impl.print;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.print.PrintSettingProvider;
import com.wanmi.sbc.customer.api.request.print.PrintSettingSaveRequest;
import com.wanmi.sbc.customer.print.model.root.PrintSetting;
import com.wanmi.sbc.customer.print.service.PrintSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 打印设置-打印设置添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/12 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class PrintSettingController implements PrintSettingProvider {

    @Autowired
    private PrintSettingService printSettingService;

    @Override
    public BaseResponse modifyPrintSetting(@RequestBody @Valid PrintSettingSaveRequest request){
        PrintSetting printSetting = new PrintSetting();
        KsBeanUtil.copyPropertiesThird(request, printSetting);
        printSettingService.updateSettings(printSetting);
        return BaseResponse.SUCCESSFUL();
    }
}
