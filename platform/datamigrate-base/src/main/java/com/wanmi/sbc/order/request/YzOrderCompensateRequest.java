package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

@ApiModel
@Data
public class YzOrderCompensateRequest implements Serializable {

    private static final long serialVersionUID = -8183973077128671781L;

    @ApiModelProperty("页码")
    private Integer pageNo = NumberUtils.INTEGER_ZERO;
}
