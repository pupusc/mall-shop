package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.print.PrintSettingProvider;
import com.wanmi.sbc.customer.api.provider.print.PrintSettingQueryProvider;
import com.wanmi.sbc.customer.api.request.print.PrintSettingByStoreIdRequest;
import com.wanmi.sbc.customer.api.request.print.PrintSettingSaveRequest;
import com.wanmi.sbc.customer.api.response.print.PrintSettingResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "StorePrintSettingController", description = "店铺打印服务API")
@RestController
@RequestMapping("/print/setting")
public class StorePrintSettingController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PrintSettingQueryProvider printSettingQueryProvider;

    @Autowired
    private PrintSettingProvider printSettingProvider;

    /**
     * 查询打印设置
     *
     * @return
     */
    @ApiOperation(value = "查询打印设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<PrintSettingResponse> queryPrintSetting() {
        return printSettingQueryProvider.getPrintSettingByStoreId(
                PrintSettingByStoreIdRequest.builder()
                        .storeId(commonUtil.getStoreId())
                .build()
        );
    }

    /**
     * 更新打印设置
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "更新打印设置")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse savePrintSetting(@RequestBody PrintSettingSaveRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        return printSettingProvider.modifyPrintSetting(request);
    }
}
