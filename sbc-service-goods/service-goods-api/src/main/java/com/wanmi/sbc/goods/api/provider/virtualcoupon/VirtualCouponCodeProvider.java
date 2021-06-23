package com.wanmi.sbc.goods.api.provider.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.*;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeAddResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>券码保存服务Provider</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@FeignClient(value = "${application.goods.name}", contextId = "VirtualCouponCodeProvider")
public interface VirtualCouponCodeProvider {

    /**
     * 新增券码API
     *
     * @param virtualCouponCodeAddRequest 券码新增参数结构 {@link VirtualCouponCodeAddRequest}
     * @return 新增的券码信息 {@link VirtualCouponCodeAddResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/add")
    BaseResponse add(@RequestBody @Valid VirtualCouponCodeAddRequest virtualCouponCodeAddRequest);

    /**
     * 单个删除券码API
     *
     * @param virtualCouponCodeDelByIdRequest 单个删除参数结构 {@link VirtualCouponCodeDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid VirtualCouponCodeDelByIdRequest virtualCouponCodeDelByIdRequest);

    /**
     * 批量删除券码API
     *
     * @param virtualCouponCodeDelByIdListRequest 批量删除参数结构 {@link VirtualCouponCodeDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid VirtualCouponCodeDelByIdListRequest virtualCouponCodeDelByIdListRequest);

    /**
     * 发券接口(关联订单)-下单时调用
     *
     * @param request {@link VirtualCouponCodeByIdRequest}
     * @return 券码列表 {@link VirtualCouponCodeByIdResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/link-order")
    BaseResponse<VirtualCouponCodeListResponse> linkOrder(@RequestBody @Valid VirtualCouponCodeLinkOrderRequest request);

    /**
     * 发券接口(关联订单)-订单失效时调用
     *
     * @param request {@link VirtualCouponCodeByIdRequest}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/unlink-order")
    BaseResponse unlinkOrder(@RequestBody @Valid VirtualCouponCodeUnLinkOrderRequest request);

    /**
     * 初始化卡券券码的缓存--导入时调用
     */
    @GetMapping("/goods/${application.goods.version}/virtualcouponcode/init-coupon-code-no-cache/{couponId}")
    BaseResponse initCouponCodeNoCache(@PathVariable("couponId") Long couponId);

    /**
     * 处理过期的券码
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/virtualcoupon/expire-virtual-coupon-code")
    BaseResponse expireVirtualCouponCode();

}

