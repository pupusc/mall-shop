package com.wanmi.sbc.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(description = "审核", tags = "RiskVerifyController")
@RestController("riskVerifyController")
@RequestMapping("/audit")
public class RiskVerifyController {

    @Autowired
    private GoodsProvider goodsProvider;

    @ApiOperation(value = "图片审核回调")
    @PostMapping(value = "/image/callback")
    public BaseResponse auditImage(@RequestBody ImageVerifyRequest request) {
        if(request == null || StringUtils.isEmpty(request.getCustomerId())){
            return  BaseResponse.FAILED();
        }


        return BaseResponse.success(null);
    }
}
