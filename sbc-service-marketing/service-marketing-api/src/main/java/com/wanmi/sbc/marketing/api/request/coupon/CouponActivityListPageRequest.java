package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityListPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 4243718077145628609L;


    @ApiModelProperty(value = "活动ID集合")
    private List<String> activityIds;

}
