package com.wanmi.sbc.customer.levelrights.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询是否存在符合会员等级权益发券的会员，发放优惠券;定时任务每天夜里两点执行
 */
@Service
public class CustomerLevelRightsCouponAnalyseService {

    @Autowired
    private CustomerLevelRightsService customerLevelRightsService;

    public CustomerLevelRightsListResponse queryIssueCouponsData() {
        // 会员等级权益查询条件（1、权益类型为券礼包 2、未删除）
        CustomerLevelRightsQueryRequest levelRightsQueryRequest = new CustomerLevelRightsQueryRequest();
        levelRightsQueryRequest.setRightsType(LevelRightsType.COUPON_GIFT);
        levelRightsQueryRequest.setDelFlag(DeleteFlag.NO);
        List<CustomerLevelRights> couponRightsList = customerLevelRightsService.list(levelRightsQueryRequest);
        // 判断优惠券发放方式（到达该等级发放一次/每月x号发放）,筛选出需要当天发放优惠券的权益
        List<CustomerLevelRights> rightsList = couponRightsList.stream().filter(rights -> {
            JSONObject rightsRule = JSONObject.parseObject(rights.getRightsRule());
            return rightsRule.get("type").equals("issueMonthly")
                    && Integer.parseInt(rightsRule.get("issueDate").toString()) == LocalDate.now().getDayOfMonth();
        }).collect(Collectors.toList());
        List<CustomerLevelRightsVO> newList = rightsList.stream().map(entity -> customerLevelRightsService.wrapperVo(entity)).collect(Collectors.toList());
        return new CustomerLevelRightsListResponse(newList);
    }

}
