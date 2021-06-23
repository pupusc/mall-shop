package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.EmailConfigProvider;
import com.wanmi.sbc.setting.api.request.EmailConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.EmailConfigQueryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 平台端-邮箱接口配置
 */
@Api(tags = "BossEmailConfigController", description = "平台端-邮箱接口配置API")
@RestController
@RequestMapping("/boss/emailConfig")
public class BossEmailConfigController {


    @Autowired
    private EmailConfigProvider emailConfigProvider;

    /**
     * 查询BOSS管理后台邮箱接口配置
     *
     * @return
     */
    @ApiOperation(value = "查询BOSS管理后台邮箱接口配置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<EmailConfigQueryResponse> queryEmailConfig() {
        return emailConfigProvider.queryEmailConfig();
    }

    /**
     * 更新邮箱接口配置
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "更新邮箱接口配置")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse<EmailConfigQueryResponse> modifyEmailConfig(@Valid @RequestBody EmailConfigModifyRequest request) {
        request.setUpdateTime(LocalDateTime.now());
        return emailConfigProvider.modifyEmailConfig(request);
    }

}
