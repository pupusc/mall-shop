package com.wanmi.sbc.umengpushconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigAddRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigAddResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "友盟push接口配置管理API", tags = "UmengPushConfigController")
@RestController
@RequestMapping(value = "/umengpushconfig")
public class UmengPushConfigController {

    @Autowired
    private UmengPushConfigQueryProvider umengPushConfigQueryProvider;

    @Autowired
    private UmengPushConfigSaveProvider umengPushConfigSaveProvider;

    @ApiOperation(value = "查询友盟push接口配置")
    @GetMapping("/getConfig")
    public BaseResponse<UmengPushConfigByIdResponse> getConfig() {
        UmengPushConfigByIdRequest idReq = new UmengPushConfigByIdRequest();
        idReq.setId(-1);
        return umengPushConfigQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增、修改友盟push接口配置")
    @PostMapping("/add")
    public BaseResponse<UmengPushConfigAddResponse> add(@RequestBody @Valid UmengPushConfigAddRequest addReq) {
        return umengPushConfigSaveProvider.add(addReq);
    }
}
