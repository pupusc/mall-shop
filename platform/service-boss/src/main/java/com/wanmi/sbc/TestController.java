package com.wanmi.sbc;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandPageRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandResponse;
import com.wanmi.sbc.job.GoodsPriceUpdateJobHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private GoodsPriceUpdateJobHandler job;
    @RequestMapping(value = "/ts", method = RequestMethod.GET)
    public BaseResponse test() {
        try {
            job.execute("");
        }catch (Exception e){

        }
        return BaseResponse.SUCCESSFUL();
    }
}
