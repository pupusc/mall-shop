package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchModifyRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchSendCouponRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeByCustomizeProviderRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeReturnByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponFetchRequest;
import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import com.wanmi.sbc.marketing.coupon.mq.PaidCouponsMqService;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityConfigService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeCopyService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import com.wanmi.sbc.marketing.coupon.service.CouponMarketingCustomerScopeService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CouponCodeController implements CouponCodeProvider {

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private CouponCodeCopyService couponCodeCopyService;

    @Autowired
    private CouponMarketingCustomerScopeService couponMarketingCustomerScopeService;

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private CouponInfoRepository couponInfoRepository;



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


    @Override
    public BaseResponse sendCouponCodeByCustomize(@RequestBody List<CouponCodeByCustomizeProviderRequest> couponCodeByCustomizeProviderRequestList) {
        log.info("****************手动发放优惠券   开始 ****************");
        for (CouponCodeByCustomizeProviderRequest param : couponCodeByCustomizeProviderRequestList) {
            log.info("手动发放优惠券：activityId:{} customerId:{}", param.getActivityId(), param.getCustomerId());
            List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(param.getActivityId());
            // 根据配置查询需要发放的优惠券列表
            List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                    CouponActivityConfig::getCouponId).collect(Collectors.toList()));
//            log.info("根据配置查询需要发放的优惠券列表:{}",couponInfoList);
            // 组装优惠券发放数据
            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
            getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
                if (item.getCouponId().equals(config.getCouponId())) {
                    item.setTotalCount(config.getTotalCount());
                }
            })).collect(Collectors.toList());
            couponCodeService.sendBatchCouponCodeByCustomer(getCouponGroupResponse, param.getCustomerId(), param.getActivityId());
        }
        log.info("****************手动发放优惠券   结束 ****************");
        return BaseResponse.SUCCESSFUL();
    }

}
