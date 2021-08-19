package com.wanmi.sbc.vas.iep;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.api.constants.iep.IepServiceErrorCode;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingQueryProvider;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingTopOneResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 企业购设置
 */
@Api(tags = "IepSettingQueryController", description = "增值服务-企业购设置查询 API")
@RestController
@RequestMapping("/vas/iep/setting")
public class IepSettingQueryController {


    @Autowired
    private IepSettingQueryProvider iepSettingQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "查询企业购设置信息")
    @GetMapping("/detail")
    public BaseResponse<IepSettingTopOneResponse> findTopOne() {
        return iepSettingQueryProvider.findTopOne();
    }

    @ApiOperation(value = "查询缓存中的企业购设置信息提供给用户")
    @GetMapping("/cache")
    public BaseResponse<IepSettingTopOneResponse> findCacheForCustomer() {
        // 首先判断是否购买企业购增值服务,未购买或未设置自动抛出异常
        return BaseResponse.success(new IepSettingTopOneResponse(commonUtil.getIepSettingInfo()));

    }
}
