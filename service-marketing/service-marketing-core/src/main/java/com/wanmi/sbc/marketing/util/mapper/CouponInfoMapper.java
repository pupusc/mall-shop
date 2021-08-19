package com.wanmi.sbc.marketing.util.mapper;

import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponInfoMapper {

    @Mappings({})
    CouponInfoVO couponInfoToCouponInfoVO(CouponInfo couponInfo);

    @Mappings({})
    List<CouponInfoVO> couponInfoToCouponInfoVO(List<CouponInfo> couponInfo);
}
