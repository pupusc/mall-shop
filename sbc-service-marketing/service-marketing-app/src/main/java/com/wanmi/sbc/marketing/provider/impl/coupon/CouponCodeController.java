package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchModifyRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchSendCouponRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeReturnByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponFetchRequest;
import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeCopyService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import com.wanmi.sbc.marketing.coupon.service.CouponMarketingCustomerScopeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>对优惠券码操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@Validated
@RestController
public class CouponCodeController implements CouponCodeProvider {

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private CouponCodeCopyService couponCodeCopyService;

    @Autowired
    private CouponMarketingCustomerScopeService couponMarketingCustomerScopeService;

    /**
     * 领取优惠券
     *
     * @param request 优惠券领取请求结构 {@link CouponFetchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse fetch(@RequestBody @Valid CouponFetchRequest request){
        couponCodeService.customerFetchCoupon(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量更新券码使用状态
     *
     * @param request 批量修改请求结构 {@link CouponCodeBatchModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse batchModify(@RequestBody @Valid CouponCodeBatchModifyRequest request){
        couponCodeService.batchModify(request.getModifyDTOList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据id撤销优惠券使用
     *
     * @param request 包含id的撤销使用请求结构 {@link CouponCodeReturnByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse returnById(@RequestBody @Valid CouponCodeReturnByIdRequest request){
        couponCodeService.returnCoupon(request.getCouponCodeId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse precisionVouchers(@RequestBody @Valid CouponCodeBatchSendCouponRequest request){
        List<String> customerIds = request.getCustomerIds();
        List<CouponActivityConfigAndCouponInfoDTO> list = request.getList();
        if (CollectionUtils.isEmpty(customerIds)){
            List<CouponMarketingCustomerScope> customerScopes = couponMarketingCustomerScopeService.findByActivityId(list.get(0).getActivityId());
            customerIds = customerScopes.stream().map(CouponMarketingCustomerScope::getCustomerId).collect(Collectors.toList());
        }
        couponCodeService.precisionVouchers(customerIds,list);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 数据迁移：旧coupon_code按照新的分表规则进行拆分保存至新表中
     * @return
     */
    @Override
    public BaseResponse dataMigrationFromCouponCode() {
        Integer pageNum = NumberUtils.INTEGER_ZERO;
        Integer pageSize = 5000;
        int result = NumberUtils.INTEGER_ZERO;
        while (Boolean.TRUE) {
            int num = couponCodeCopyService.dataMigrationFromCouponCode(pageNum,pageSize);
            if (num == 0 ){
                break;
            }
            result += num;
        }
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse sendBatchCouponCodeByCustomerList(@RequestBody CouponCodeBatchSendCouponRequest request) {

        return BaseResponse.success(this.couponCodeService.sendBatchCouponCodeByCustomerList(request));
    }


}
