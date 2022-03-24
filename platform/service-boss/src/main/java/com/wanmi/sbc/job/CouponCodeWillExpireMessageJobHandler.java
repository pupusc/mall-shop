package com.wanmi.sbc.job;


import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerWithOpenIdPageRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplerVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeByCustomerIdsRequest;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券即将过期通知
 */
@Component
@JobHandler(value = "couponCodeWillExpireMessageJobHandler")
@Slf4j
public class CouponCodeWillExpireMessageJobHandler extends IJobHandler {

    //分布式锁名称
    private static final String COUPON_CODE_WILL_EXPIRE_MESSAGE_LOCK = "COUPON_CODE_WILL_EXPIRE_MESSAGE_LOCK";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CouponCodeQueryProvider couponCodeQueryProvider;

    @Autowired
    private CommonController wxCommonController;

    @Value("{$wx.coupon.will.expire.send.message.templateId}")
    private String couponSendMessageTemplateId;

    @Value("{$coupon.list.url}")
    private String couponListUrl;

    @Override
    public ReturnT<String> execute(String s) throws Exception {

        RLock lock = redissonClient.getLock(COUPON_CODE_WILL_EXPIRE_MESSAGE_LOCK);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("优惠券即将过期通知start");
        int pageSize = 500;
        LocalDateTime now = LocalDateTime.now();
        try {
            //查询数量
            BaseResponse<Long> countResponse = customerQueryProvider.countCustomerWithOpenId();
            if (countResponse == null || countResponse.getContext() == null || countResponse.getContext().compareTo(1L) < 0) {
                log.info("获取有微信小程序openId的数量是0");
                return SUCCESS;
            }
            int pageNum = 0;
            for (int i = 0; i < countResponse.getContext(); i += pageSize) {
                log.info("优惠券即将过期通知,共{}条数据,当前第{}页", countResponse.getContext(), pageNum);
                //获取小程序openId
                CustomerWithOpenIdPageRequest customerWithOpenIdPageRequest = new CustomerWithOpenIdPageRequest();
                customerWithOpenIdPageRequest.setPageNum(pageNum);
                customerWithOpenIdPageRequest.setPageSize(pageSize);
                BaseResponse<MicroServicePage<CustomerSimplerVO>> customerPage = customerQueryProvider.listCustomerWithOpenId(customerWithOpenIdPageRequest);
                ++pageNum;
                if (customerPage == null || customerPage.getContext() == null || CollectionUtils.isEmpty(customerPage.getContext().getContent())) {
                    continue;
                }
                //根据customerId获取即将过期优惠券
                List<String> customerIds = customerPage.getContext().getContent().stream().map(CustomerSimplerVO::getCustomerId).collect(Collectors.toList());
                CouponCodeByCustomerIdsRequest customerIdsRequest = new CouponCodeByCustomerIdsRequest();
                customerIdsRequest.setCustomerId(customerIds);
                customerIdsRequest.setWillExpireDate(now.plusDays(3L));
                BaseResponse<List<CouponCodeVO>> couponResponse = couponCodeQueryProvider.listWillExpireByCustomerIds(customerIdsRequest);
                if (couponResponse == null || CollectionUtils.isEmpty(couponResponse.getContext())) {
                    continue;
                }
                //通知消息
                couponResponse.getContext().forEach(couponCode -> {
                    this.sendMessage(customerPage.getContext().getContent(), couponCode);
                });
            }
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("优惠券即将过期通知定时任务失败", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    private void sendMessage(List<CustomerSimplerVO> customerList, CouponCodeVO couponCode) {
        try {
            Optional<CustomerSimplerVO> optionalCustomer = customerList.stream().filter(p -> p.getCustomerId().equals(couponCode.getCustomerId())).findFirst();
            if (!optionalCustomer.isPresent()) {
                return;
            }
            WxSendMessageRequest request = new WxSendMessageRequest();
            request.setOpenId(optionalCustomer.get().getWxMiniOpenId());
            request.setTemplateId(couponSendMessageTemplateId);
            request.setUrl(couponListUrl);
            Map<String, Map<String, String>> map = new HashMap<>();
            map.put("thing1", new HashMap<String, String>() {{
                put("value", couponCode.getCouponName());
            }});
            map.put("date2", new HashMap<String, String>() {{
                put("value", DateUtil.format(couponCode.getEndTime(), DateUtil.FMT_TIME_1));
            }});
            map.put("thing3", new HashMap<String, String>() {{
                put("value", "您的抵用券即将过期，快去看看吧！");
            }});
            map.put("thing4", new HashMap<String, String>() {{
                put("value", "活动进行中");
            }});
            request.setData(map);
            BaseResponse<WxResponseBase> response = wxCommonController.sendMessage(request);
            log.info("微信小程序优惠券即将过期发送消息request:{},response:{}", request, response);
        } catch (Exception e) {
            log.error("微信小程序优惠券即将过期发送消息失败,couponCode:{}", couponCode, e);
        }
    }

}

