package com.wanmi.sbc.vas;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.api.response.VASSettingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: songhanlin
 * @Date: Created In 14:45 2020/2/28
 * @Description: 增值服务 API
 */
@Api(tags = "VASSettingController", description = "增值服务 API")
@RestController("VASSettingController")
@RequestMapping("/vas/setting")
public class VASSettingController {

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "查询所有增值服务")
    @GetMapping("/list")
    public BaseResponse<VASSettingResponse> queryAllVAS() {
        return BaseResponse.success(VASSettingResponse.builder().services(commonUtil.getAllServices()).build());
    }
}
