package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponInfoDTO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CouponInfosQueryResponse {

    @ApiModelProperty(value = "优惠券列表")
    private List<CouponInfoVO> couponCodeList;
}
