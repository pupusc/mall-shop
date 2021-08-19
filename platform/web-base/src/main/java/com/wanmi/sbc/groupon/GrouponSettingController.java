package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.grouponsetting.GrouponSettingQueryProvider;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenli on 2019/5/21.
 */

/**
 * 拼团设置控制器
 */
@RestController
@RequestMapping("/groupon/setting")
@Api(description = "S2B web公用-拼团设置", tags = "GrouponSettingController")
public class GrouponSettingController {

    @Autowired
    private GrouponSettingQueryProvider grouponSettingQueryProvider;

    /**
     * 查询拼团设置
     * @return
     */
    @ApiModelProperty(value = "查询拼团设置")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public BaseResponse<GrouponSettingPageResponse> findOne() {
       return grouponSettingQueryProvider.getSetting();
    }
}
