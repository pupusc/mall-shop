package com.wanmi.sbc.open.vo;

import com.wanmi.sbc.marketing.api.request.Source;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
public class SendCouponGroupWithCreateUserVO extends BaseReqVO implements Serializable {

    @NotNull
    private FanUserVO fanUser;

    @ApiModelProperty(value = "活动id")
    @NotNull
    private String activityId;

    @ApiModelProperty(value = "来源")
    private Source source;

}
