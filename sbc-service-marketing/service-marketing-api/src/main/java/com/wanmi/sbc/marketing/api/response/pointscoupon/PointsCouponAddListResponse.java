package com.wanmi.sbc.marketing.api.response.pointscoupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @since 2019/5/14
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponAddListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "积分优惠券信息")
    private   List<String> activityIdList;
}
