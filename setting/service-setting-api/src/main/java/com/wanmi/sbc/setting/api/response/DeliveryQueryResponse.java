package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@ApiModel
@Data
public class DeliveryQueryResponse implements Serializable {
    private static final long serialVersionUID = 4915215094630633130L;
    /**
     * 物流详情
     */
    @ApiModelProperty(value = "物流详情")
    private List<Map<Object, Object>> orderList = new ArrayList<>();
}
