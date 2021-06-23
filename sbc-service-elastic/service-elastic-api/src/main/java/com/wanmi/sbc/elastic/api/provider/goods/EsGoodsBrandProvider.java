package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandSaveRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsBrandAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/10 10:22
 * @description <p> </p>
 */

@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsBrandProvider")
public interface EsGoodsBrandProvider {

    /**
     * 批量新增品牌
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/brand/add")
    BaseResponse<EsGoodsBrandAddResponse> addGoodsBrandList(@RequestBody @Valid EsGoodsBrandSaveRequest request);


    /**
     * 品牌初始化
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/brand/init")
    BaseResponse init(EsGoodsBrandPageRequest request);


}
