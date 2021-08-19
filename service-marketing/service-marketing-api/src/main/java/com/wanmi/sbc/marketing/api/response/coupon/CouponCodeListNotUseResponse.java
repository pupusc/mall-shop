package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCodeListNotUseResponse {

    @ApiModelProperty(value = "优惠券券码列表")
    private List<CouponCodeVO> couponCodeList;
}
