package com.wanmi.sbc.open.vo;

import com.wanmi.sbc.marketing.api.request.Source;
import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class CouponRecycleReqVO extends BaseReqVO implements Serializable {

    @ApiModelProperty
    private String fanUserId;

    @ApiModelProperty(value = "来源")
    private Source source;

}
