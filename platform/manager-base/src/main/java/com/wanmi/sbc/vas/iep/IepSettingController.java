package com.wanmi.sbc.vas.iep;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingProvider;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingQueryProvider;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingModifyRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingModifyResponse;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingTopOneResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 20:31 2020/3/2
 * @Description: 企业购设置
 */
@Api(tags = "IepSettingController", description = "增值服务-企业购设置 API")
@RestController("IepSettingController")
@RequestMapping("/vas/iep/setting")
public class IepSettingController {

    @Autowired
    private IepSettingProvider iepSettingProvider;

    @Autowired
    private IepSettingQueryProvider iepSettingQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "查询企业购设置信息")
    @GetMapping
    public BaseResponse<IepSettingTopOneResponse> findTopOne() {
        return iepSettingQueryProvider.findTopOne();
    }

    @ApiOperation(value = "修改企业购设置信息")
    @PutMapping
    public BaseResponse<IepSettingModifyResponse> modify(@RequestBody @Valid IepSettingModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        return iepSettingProvider.modify(request);
    }
}
