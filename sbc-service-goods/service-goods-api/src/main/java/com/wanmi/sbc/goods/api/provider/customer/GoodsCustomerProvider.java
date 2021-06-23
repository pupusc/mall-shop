package com.wanmi.sbc.goods.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.customer.GoodsCustomerNumRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/6 10:08
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCustomerProvider")
public interface GoodsCustomerProvider {

    /**
     * 更新商品的客户数量
     * @param goodsCustomerNumRequest  {@link GoodsCustomerNumRequest}
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/customer/modify")
    BaseResponse modify(@RequestBody @Valid GoodsCustomerNumRequest goodsCustomerNumRequest);

}
