package com.wanmi.sbc.open.vo;

import com.wanmi.sbc.marketing.api.request.Source;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class CouponListReqVO extends BaseReqVO implements Serializable {

    @ApiModelProperty
    private Integer fanUserId;

    @ApiModelProperty(value = "来源")
    private String activityId;

}
