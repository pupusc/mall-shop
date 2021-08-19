package com.wanmi.sbc.goods.api.provider.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponAddRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponGoodsRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>卡券保存服务Provider</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@FeignClient(value = "${application.goods.name}", contextId = "VirtualCouponProvider")
public interface VirtualCouponProvider {

    /**
     * 新增卡券API
     *
     * @param virtualCouponAddRequest 卡券新增参数结构 {@link VirtualCouponAddRequest}
     * @return 新增的卡券信息 {@link VirtualCouponAddResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcoupon/add")
    BaseResponse<VirtualCouponAddResponse> add(@RequestBody @Valid VirtualCouponAddRequest virtualCouponAddRequest);

    /**
     * 卡券关联商品
     *
     * @param request 卡券新增参数结构 {@link VirtualCouponAddRequest}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcoupon/link-goods")
    BaseResponse linkGoods(@RequestBody @Valid VirtualCouponGoodsRequest request);

    /**
     * 卡券取消关联商品
     *
     * @param request 卡券新增参数结构 {@link VirtualCouponAddRequest}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcoupon/unlink-goods")
    BaseResponse unlinkGoods(@RequestBody @Valid VirtualCouponGoodsRequest request);
}

