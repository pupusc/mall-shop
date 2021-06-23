package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponCodeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CouponCodeListByConditionResponse {

    @ApiModelProperty(value = "优惠券券码列表")
    private List<CouponCodeDTO> couponCodeList;
}
