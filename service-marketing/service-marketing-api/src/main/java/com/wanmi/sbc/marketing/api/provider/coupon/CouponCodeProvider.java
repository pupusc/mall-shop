package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>对优惠券码操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCodeProvider")
public interface CouponCodeProvider {

    /**
     * 领取优惠券
     *
     * @param request 优惠券领取请求结构 {@link CouponFetchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/fetch")
    BaseResponse fetch(@RequestBody @Valid CouponFetchRequest request);

    /**
     * 批量更新券码使用状态
     *
     * @param request 批量修改请求结构 {@link CouponCodeBatchModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/batch-modify")
    BaseResponse batchModify(@RequestBody @Valid CouponCodeBatchModifyRequest request);

    /**
     * 根据id撤销优惠券使用
     *
     * @param request 包含id的撤销使用请求结构 {@link CouponCodeReturnByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/return-by-id")
    BaseResponse returnById(@RequestBody @Valid CouponCodeReturnByIdRequest request);

    /**
     * 精准发券
     * @param request
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/precision-vouchers")
    BaseResponse precisionVouchers(@RequestBody @Valid CouponCodeBatchSendCouponRequest request);

    /**
     * 数据迁移：旧coupon_code按照新的分表规则进行拆分保存至新表中
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/data-migration")
    BaseResponse dataMigrationFromCouponCode();

    /**
     * 批量发券
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/send-batch")
    BaseResponse sendBatchCouponCodeByCustomerList(@RequestBody @Valid CouponCodeBatchSendCouponRequest request);

    /**
     * 手动发放优惠券
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/send-by-customize")
    BaseResponse sendCouponCodeByCustomize(@RequestBody List<CouponCodeByCustomizeProviderRequest> couponCodeByCustomizeProviderRequestList);

    /**
     * 手动文件发放优惠券
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/send-by-file-customize")
    BaseResponse sendCouponCodeByFileCustomize(@RequestBody CouponCodeByFileCustomizeProviderRequest couponCodeByFileCustomizeProviderRequest);


    /**
     * 根据商品ID发放优惠券
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/send-by-goodsIds")
    BaseResponse sendCouponCodeByGoodsIds(@RequestBody CouponCodeByGoodsIdsRequest couponCodeByGoodsIdsRequest);


}
