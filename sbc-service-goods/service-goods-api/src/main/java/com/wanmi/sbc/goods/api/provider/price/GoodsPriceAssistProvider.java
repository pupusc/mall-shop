package com.wanmi.sbc.goods.api.provider.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.price.GoodsPriceSetBatchByIepRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsPriceSetBatchByIepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品设价调整接口</p>
 * Created by of628 on 2020-03-04-下午4:47.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsPriceAssistProvider")
public interface GoodsPriceAssistProvider {

    /**
     * 企业购商品批量设置企业会员价
     * @param request 包含商品信息和区间设价信息{@link GoodsPriceSetBatchByIepRequest}
     * @return response 包含商品信息和区间设价信息的结果{@link GoodsPriceSetBatchByIepResponse}
     */
    @PostMapping("/goods/${application.goods.version}/price/goods-price-set-by-iep")
    BaseResponse<GoodsPriceSetBatchByIepResponse> goodsPriceSetBatchByIep(@RequestBody @Valid GoodsPriceSetBatchByIepRequest request);
}
