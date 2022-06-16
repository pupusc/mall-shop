package com.soybean.mall.cart;

import com.soybean.mall.cart.vo.PromoteInfoReqVO;
import com.soybean.mall.cart.vo.PromoteInfoResVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseListRequest;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Liang Jun
 * @desc 采购
 * @date 2022-06-15 11:14:00
 */
@Slf4j
@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    /**
     * 获取购物车信息
     */
    @PostMapping(value = "/cartInfo")
    public BaseResponse<PurchaseListResponse> cartInfo(@RequestBody PurchaseListRequest request) {
        return null;
    }

    @PostMapping(value = "/promoteInfo")
    public BaseResponse<PromoteInfoResVO> promoteInfo(@Valid @RequestBody PromoteInfoReqVO param) {
        return null;
    }
}
