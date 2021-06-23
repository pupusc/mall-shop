package com.wanmi.sbc.marketing.api.provider.pointscoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.pointscoupon.*;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponAddListResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponAddResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponModifyResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponSendCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>积分兑换券表保存服务Provider</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@FeignClient(value = "${application.marketing.name}", contextId = "PointsCouponSaveProvider")
public interface PointsCouponSaveProvider {

    /**
     * 新增积分兑换券表API
     *
     * @param pointsCouponAddRequest 积分兑换券表新增参数结构 {@link PointsCouponAddRequest}
     * @return 新增的积分兑换券表信息 {@link PointsCouponAddResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/add")
    BaseResponse<PointsCouponAddResponse> add(@RequestBody @Valid PointsCouponAddRequest pointsCouponAddRequest);

    /**
     * 批量新增积分兑换券表API
     *
     * @param pointsCouponAddListRequest 积分兑换券表新增参数结构 {@link PointsCouponAddRequest}
     * @return 批量新增的积分兑换券表信息{@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/batchAdd")
    BaseResponse<PointsCouponAddListResponse> batchAdd(@RequestBody @Valid PointsCouponAddListRequest pointsCouponAddListRequest);

    /**
     * 修改积分兑换券表API
     *
     * @param pointsCouponModifyRequest 积分兑换券表修改参数结构 {@link PointsCouponModifyRequest}
     * @return 修改的积分兑换券表信息 {@link PointsCouponModifyResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/modify")
    BaseResponse<PointsCouponModifyResponse> modify(@RequestBody @Valid PointsCouponModifyRequest pointsCouponModifyRequest);

    /**
     * 修改积分兑换券状态API
     *
     * @param request 积分商品表修改状态参数结构 {@link PointsCouponSwitchRequest}
     * @return 状态结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/modify-status")
    BaseResponse modifyStatus(@RequestBody @Valid PointsCouponSwitchRequest request);

    /**
     * 单个删除积分兑换券表API
     *
     * @param pointsCouponDelByIdRequest 单个删除参数结构 {@link PointsCouponDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid PointsCouponDelByIdRequest pointsCouponDelByIdRequest);

    /**
     * 会员兑换积分优惠券API
     *
     * @param pointsCouponFetchRequest 会员兑换积分优惠券参数结构 {@link PointsCouponFetchRequest}
     * @return 兑换结果 {@link BaseResponse}
     * @author minchen
     */
    @PostMapping("/goods/${application.goods.version}/pointscoupon/exchange")
    BaseResponse<PointsCouponSendCodeResponse> exchangePointsCoupon(@RequestBody @Valid PointsCouponFetchRequest pointsCouponFetchRequest);

}

