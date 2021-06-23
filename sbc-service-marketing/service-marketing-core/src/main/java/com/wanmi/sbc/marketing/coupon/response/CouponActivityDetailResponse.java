package com.wanmi.sbc.marketing.coupon.response;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivity;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CouponActivityDetailResponse implements Serializable {


    private static final long serialVersionUID = 7266785219929124711L;

    CouponActivity couponActivity;

    List<CouponActivityConfig> couponActivityConfigList;

    List<CouponInfo> couponInfoList;

    //会员等级
    List<CustomerLevelVO> customerLevelList;
    //活动目标客户范围
    List<CouponMarketingCustomerScope> couponMarketingCustomerScope;
    //目标客户信息
    List<CustomerVO>  customerDetailVOS;

}
