package com.wanmi.sbc.distribute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSimSettingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分销设置controller
 *
 * @Author: liutao
 * @Date: Created In 下午1:44 2019/4/22
 * @Description:
 */
@Api(description = "分销设置服务" ,tags ="DistributionSettingController")
@RestController
@RequestMapping("/distribution-setting")
@Validated
public class DistributionSettingController {

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    /**
     * 查询分销设置API
     *
     * @return
     */
    @ApiOperation(value = "查询分销设置")
    @RequestMapping(method = RequestMethod.GET)
    BaseResponse<DistributionSimSettingResponse> findOne() {
        return distributionSettingQueryProvider.findOneSetting();
    }
}
