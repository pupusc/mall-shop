package com.wanmi.sbc.goods.api.provider.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 14:14
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsInfoSiteQueryProvider")
public interface GoodsInfoSiteQueryProvider {

    /**
     * 获取商品详情
     * 计算会员和订货区间
     * @param goodsInfoRequest {@link GoodsInfoRequest }
     * @return 商品详情 {@link GoodsInfoDetailByGoodsInfoResponse }
     */
    @PostMapping("/goods/${application.goods.version}/info/get-by-goods-info")
    BaseResponse<GoodsInfoDetailByGoodsInfoResponse> getByGoodsInfo(@RequestBody @Valid GoodsInfoRequest goodsInfoRequest);
}
