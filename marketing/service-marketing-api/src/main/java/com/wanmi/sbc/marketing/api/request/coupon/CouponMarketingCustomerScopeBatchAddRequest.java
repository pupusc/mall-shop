package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponMarketingCustomerScopeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 批量新增优惠券活动目标客户作用范围
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponMarketingCustomerScopeBatchAddRequest implements Serializable {


    private static final long serialVersionUID = -3522780831277764475L;
    /**
     * 优惠券活动目标客户作用范围内容 {@link CouponMarketingCustomerScopeDTO}
     */
    @ApiModelProperty(value = "优惠券活动目标客户作用范围列表")
    @NotEmpty
    private List<CouponMarketingCustomerScopeDTO> scopeDTOList;

}
