package com.wanmi.sbc.marketing.util.mapper;

import com.wanmi.sbc.marketing.bean.vo.CouponActivityBaseVO;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponActivityMapper {

    @Mappings({})
    CouponActivityVO couponActivityToCouponActivityVO(CouponActivity couponActivity);

    @Mappings({})
    List<CouponActivityBaseVO> couponActivityToCouponActivityBaseVO(List<CouponActivity> couponActivity);
}
