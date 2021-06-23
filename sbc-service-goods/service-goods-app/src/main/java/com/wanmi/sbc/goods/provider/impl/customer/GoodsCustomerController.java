package com.wanmi.sbc.goods.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.customer.GoodsCustomerProvider;
import com.wanmi.sbc.goods.api.request.customer.GoodsCustomerNumRequest;
import com.wanmi.sbc.goods.customer.service.GoodsCustomerNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/6 10:08
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsCustomerController implements GoodsCustomerProvider {

    @Autowired
    private GoodsCustomerNumService goodsCustomerNumService;

    /**
     * 更新商品的客户数量
     * @param goodsCustomerNumRequest  {@link GoodsCustomerNumRequest}
     * @return
     */

    @Override
    public BaseResponse modify(@RequestBody @Valid GoodsCustomerNumRequest goodsCustomerNumRequest){
        goodsCustomerNumService.save(goodsCustomerNumRequest);
        return BaseResponse.SUCCESSFUL();
    }

}
