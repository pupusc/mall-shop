package com.wanmi.sbc.open;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerFandengModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByAccountRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByFanDengRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengModifyCustomerRequest;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeByCustomizeProviderRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponRecycleRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeListByConditionResponse;
import com.wanmi.sbc.open.vo.CouponListReqVO;
import com.wanmi.sbc.open.vo.CouponRecycleReqVO;
import com.wanmi.sbc.open.vo.FanUserVO;
import com.wanmi.sbc.open.vo.SendCouponGroupWithCreateUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("open/coupon")
@RequiredArgsConstructor
public class OpenCouponController extends OpenBaseController {

    private final CustomerProvider customerProvider;
    private final CouponCodeQueryProvider couponCodeQueryProvider;
    private final CouponCodeProvider couponCodeProvider;
    private final CustomerQueryProvider customerQueryProvider;
    private final ExternalProvider externalProvider;

    /**
     * 领取一组优惠券 （指定优惠券活动）
     * 主站发放优惠券，用户不存在会自动创建
     *
     * @param request
     * @return
     */
    @PostMapping("sendWithCreateUser")
    public BaseResponse sendCouponGroupWithCreateUser(@RequestBody @Valid SendCouponGroupWithCreateUserVO request) {
        log.info("==>>发券：params = {}", JSON.toJSONString(request));
        checkSign();

        //=============================
        // 通过樊登信息查询商城用户
        // 不存在则创建
        //=============================
        String customerId = extractCustomerId(request.getFanUser());

        CouponCodeByCustomizeProviderRequest sendCouponRequest = new CouponCodeByCustomizeProviderRequest();
        sendCouponRequest.setActivityId(request.getActivityId());
        sendCouponRequest.setCustomerId(customerId);
        sendCouponRequest.setSource(request.getSource());
        return couponCodeProvider.sendCouponCodeByCustomize(Collections.singletonList(sendCouponRequest));
    }

    /**
     * 回收未使用优惠券
     *
     * @param request
     * @return
     */
    @PostMapping("recycle")
    public BaseResponse recycle(@RequestBody @Valid CouponRecycleReqVO request) {
        log.info("==>>回收券：params = {}", JSON.toJSONString(request));
        checkSign();

        //=============================
        // 通过樊登信息查询商城用户
        //=============================
        NoDeleteCustomerGetByAccountResponse customer = getCustomerByFanDengId(request.getFanUserId());
        if (customer == null) {
            return BaseResponse.error("用户不存在");
        }

        CouponRecycleRequest couponRecycleRequest = new CouponRecycleRequest();
        couponRecycleRequest.setCustomerId(customer.getCustomerId());
        couponRecycleRequest.setSource(request.getSource());
        return couponCodeProvider.recycleCoupon(couponRecycleRequest);
    }

    /**
     * 获取用户优惠券列表
     *
     * @param request
     * @return
     */
    @PostMapping("list")
    public BaseResponse<CouponCodeListByConditionResponse> recycle(@RequestBody @Valid CouponListReqVO request) {
        log.info("==>>回收券：params = {}", JSON.toJSONString(request));
        checkSign();

        //=============================
        // 通过樊登信息查询商城用户
        //=============================
        NoDeleteCustomerGetByAccountResponse customer = getCustomerByFanDengId(request.getFanUserId().toString());
        if (customer == null) {
            return BaseResponse.error("用户不存在");
        }

        CouponCodeQueryRequest couponCodeQueryRequest = new CouponCodeQueryRequest();
        couponCodeQueryRequest.setCustomerId(customer.getCustomerId());
        couponCodeQueryRequest.setActivityId(request.getActivityId());
        couponCodeQueryRequest.setPageSize(1000);
        return couponCodeQueryProvider.listCouponCodeByCondition(couponCodeQueryRequest);
    }

    private NoDeleteCustomerGetByAccountResponse getCustomerByFanDengId(String userId) {
        NoDeleteCustomerGetByFanDengRequest fanUserRequest = new NoDeleteCustomerGetByFanDengRequest();
        fanUserRequest.setFanDengId(userId);
        return customerQueryProvider.getNoDeleteCustomerByFanDengId(fanUserRequest).getContext();
    }

    private String extractCustomerId(FanUserVO fanUser) {
        NoDeleteCustomerGetByAccountResponse customer = getCustomerByFanDengId(fanUser.getUserNo());
        if (customer == null) {
            //根据樊登的电话去查找
            NoDeleteCustomerGetByAccountRequest accountRequest = new NoDeleteCustomerGetByAccountRequest();
            accountRequest.setCustomerAccount(fanUser.getMobile());
            customer = customerQueryProvider.getNoDeleteCustomerByAccount(accountRequest).getContext();
            if (customer != null) {
                //如果存在电话的，则吧该电话的客户信息更改成 樊登的id
                //合并用户
                customer.setFanDengUserNo(fanUser.getUserNo());
                customer.setLoginTime(LocalDateTime.now());
                CustomerFandengModifyRequest modifyRequest = new CustomerFandengModifyRequest();
                modifyRequest.setLoginIp(HttpUtil.getIpAddr());
                modifyRequest.setCustomerId(customer.getCustomerId());
                modifyRequest.setFanDengId(fanUser.getUserNo());
                customerProvider.modifyCustomerFanDengIdTime(modifyRequest);
            } else {
                //创建用户
                //如果电话和id都不存在，则直接创建用户
                FanDengModifyCustomerRequest customerRequest = FanDengModifyCustomerRequest.builder()
                        .fanDengUserNo(fanUser.getUserNo())
                        .nickName(fanUser.getNickName())
                        .customerAccount(fanUser.getMobile())
                        .profilePhoto(fanUser.getProfilePhoto()).build();
                customer = externalProvider.modifyCustomer(customerRequest).getContext();
            }
        }
        return customer.getCustomerId();
    }


}
