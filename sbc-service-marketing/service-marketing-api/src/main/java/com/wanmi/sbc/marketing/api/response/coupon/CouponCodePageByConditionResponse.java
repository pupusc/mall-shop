package com.wanmi.sbc.marketing.api.response.coupon;


import com.wanmi.sbc.marketing.bean.dto.CouponCodeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodePageByConditionResponse {

    @ApiModelProperty(value = "优惠券券码分页数据")
    private Page<CouponCodeDTO> couponCodeDTOPage;
}
