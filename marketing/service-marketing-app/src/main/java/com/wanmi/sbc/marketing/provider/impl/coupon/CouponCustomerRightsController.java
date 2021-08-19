package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsCouponAnalyseProvider;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardrightsrel.PaidCardRightsRelQueryProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerByGrowthValueRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdsRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsRelRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelPageRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelListRequest;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCustomerRightsProvider;
import com.wanmi.sbc.marketing.coupon.mq.PaidCouponMqSink;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityConfigService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>会员等级权益发放优惠券</p>
 */
@Validated
@RestController
@Slf4j
@EnableBinding(PaidCouponMqSink.class)
public class CouponCustomerRightsController implements CouponCustomerRightsProvider {

    @Autowired
    private CustomerLevelRightsCouponAnalyseProvider customerLevelRightsCouponAnalyseProvider;

    @Autowired
    private CustomerLevelRightsRelQueryProvider customerLevelRightsRelQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private PaidCardRightsRelQueryProvider paidCardRightsRelQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider  paidCardCustomerRelQueryProvider;


    @Autowired
    private PaidCouponMqSink paidCouponMqSink;
    /**
     * 会员等级权益发放优惠券
     *
     * @return
     */
    @Override
    public BaseResponse customerRightsIssueCoupons() {
        // 查询需要发放优惠券的权益
        List<CustomerLevelRightsVO> rightsList = customerLevelRightsCouponAnalyseProvider.queryIssueCouponsData()
                .getContext().getCustomerLevelRightsVOList();
        rightsList.forEach(rights -> {
            List<String> customerIds = new ArrayList<>();
            // 查询包含该权益的等级id
            List<Long> levelIds = customerLevelRightsRelQueryProvider
                    .listByRightsId(CustomerLevelRightsRelRequest.builder().rightsId(rights.getRightsId()).build())
                    .getContext()
                    .getCustomerLevelRightsRelVOList()
                    .stream()
                    .map(CustomerLevelRightsRelVO::getCustomerLevelId)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(levelIds)) {
                // 根据等级id查询等级详情信息
                List<CustomerLevelVO> customerLevels = customerLevelQueryProvider
                        .listCustomerLevelByIds(CustomerLevelByIdsRequest.builder().customerLevelIds(levelIds).build())
                        .getContext()
                        .getCustomerLevelVOList();
                // 高等级必然拥有低等级所有权益，所以只需找出包含该权益的会员等级中最低成长值即可
                Long growthValue = customerLevels.stream()
                        .min(Comparator.comparing(CustomerLevelVO::getGrowthValue))
                        .get().getGrowthValue();
                // 查询达到该成长值的会员id列表
//                List<String> customerIds = customerQueryProvider
//                        .listCustomerIdByGrowthValue(new CustomerByGrowthValueRequest(growthValue))
//                        .getContext().getCustomerIdList();
                customerIds.addAll(customerQueryProvider
                        .listCustomerIdByGrowthValue(new CustomerByGrowthValueRequest(growthValue))
                        .getContext().getCustomerIdList());

            }
/*            // 查询券礼包权益关联的优惠券活动配置列表
            List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(rights.getActivityId());
            // 根据配置查询需要发放的优惠券列表
            List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                    CouponActivityConfig::getCouponId).collect(Collectors.toList()));
            // 组装优惠券发放数据
            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
            getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
                if (item.getCouponId().equals(config.getCouponId())) {
                    item.setTotalCount(config.getTotalCount());
                }
            })).collect(Collectors.toList());
            // 遍历用户列表，批量发放优惠券
            List<GetCouponGroupResponse> finalGetCouponGroupResponse = getCouponGroupResponse;*/

            List<PaidCardRightsRelVO> rightsRelVOList = paidCardRightsRelQueryProvider.list
                    (PaidCardRightsRelListRequest.builder().rightsId(rights.getRightsId()).build()).getContext().getPaidCardRightsRelVOList();
            List<String> paidCardIdList = rightsRelVOList.stream().map(PaidCardRightsRelVO::getPaidCardId).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(paidCardIdList)){
               int pageNum = 0;
                LocalDateTime localDateTime = LocalDateTime.now();
                PaidCardCustomerRelPageRequest customerRelPageRequest = PaidCardCustomerRelPageRequest.builder().paidCardIdList(paidCardIdList)
                        .endTimeBegin(localDateTime).delFlag(DeleteFlag.NO).build();
                customerRelPageRequest.setPageSize(2000);
                while (true){
                    customerRelPageRequest.setPageNum(pageNum);
                    MicroServicePage<PaidCardCustomerRelVO> customerRelVOPage = paidCardCustomerRelQueryProvider.page(
                            customerRelPageRequest).getContext().getPaidCardCustomerRelVOPage();
                    log.info("当前查询的页数{},当前页数返回的页码{},当前返回的全部数据{}",pageNum,customerRelVOPage.getTotalPages(),customerRelVOPage);
                    customerRelVOPage.getContent().stream().forEach(item -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("customerId",  item.getCustomerId());
                        map.put("activityId", rights.getActivityId());
//                        List<CouponCode> statusCouponCode = couponCodeService.findNotUseStatusCouponCode(
//                                CouponCodeQueryRequest.builder().customerId(item.getCustomerId())
//                                        .activityId(rights.getActivityId()).build());
//                        if (CollectionUtils.isEmpty(statusCouponCode)){
//                            couponCodeService.sendBatchCouponCodeByCustomer(finalGetCouponGroupResponse, item.getCustomerId(), rights.getActivityId());
                        paidCouponMqSink.sendCoupon().send(new GenericMessage<>(JSONObject.toJSONString(map)));
//                        }
//                        log.info("本次发券用户:{}券活动id:{}", item.getCustomerId(),rights.getActivityId());
                    });
                    pageNum++;
                    if (pageNum >= customerRelVOPage.getTotalPages()){
                        break;
                    }

                }

            }
        //会员级别权益发券
      if (CollectionUtils.isNotEmpty(customerIds)){
            for (String customerId : customerIds) {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId",  customerId);
                map.put("activityId", rights.getActivityId());
                paidCouponMqSink.sendCoupon().send(new GenericMessage<>(JSONObject.toJSONString(map)));
            }
         }
        });
        log.info("发券结束");
        return BaseResponse.SUCCESSFUL();
    }

}
