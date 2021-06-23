package com.wanmi.sbc.elastic.coupon.mapper;

import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponActivityDTO;
import com.wanmi.sbc.elastic.coupon.model.root.EsCouponActivity;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityBaseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EsCouponActivityMapper {

    @Mappings({})
    EsCouponActivity couponInfoToEsCouponActivity(EsCouponActivityDTO couponInfo);


    @Mappings({})
    List<EsCouponActivity> couponInfoToEsCouponActivity(List<CouponActivityBaseVO> couponActivityBaseVOS);
}
