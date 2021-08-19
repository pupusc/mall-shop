package com.wanmi.sbc.goods.provider.impl.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponAddRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponGoodsRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponAddResponse;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>卡券保存服务接口实现</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@RestController
@Validated
public class VirtualCouponController implements VirtualCouponProvider {
    @Autowired
    private VirtualCouponService virtualCouponService;

    @Override
    public BaseResponse<VirtualCouponAddResponse> add(@RequestBody @Valid VirtualCouponAddRequest virtualCouponAddRequest) {
        VirtualCoupon virtualCoupon = KsBeanUtil.convert(virtualCouponAddRequest, VirtualCoupon.class);
        return BaseResponse.success(new VirtualCouponAddResponse(
                virtualCouponService.wrapperVo(virtualCouponService.add(virtualCoupon))));
    }

    /**
     * 卡券关联商品
     *
     * @param request 卡券新增参数结构 {@link VirtualCouponAddRequest}
     * @author 梁善
     */
    @Override
    public BaseResponse linkGoods(@RequestBody @Valid VirtualCouponGoodsRequest request) {
        virtualCouponService.linkGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 卡券取消关联商品
     *
     * @param request 卡券新增参数结构 {@link VirtualCouponAddRequest}
     * @author 梁善
     */
    @Override
    public BaseResponse unlinkGoods(@RequestBody @Valid VirtualCouponGoodsRequest request) {
        virtualCouponService.unlinkGoods(request);
        return BaseResponse.SUCCESSFUL();
    }
}

