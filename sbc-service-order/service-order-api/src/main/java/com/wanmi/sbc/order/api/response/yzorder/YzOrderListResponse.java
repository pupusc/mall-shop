package com.wanmi.sbc.order.api.response.yzorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class YzOrderListResponse implements Serializable {

    private static final long serialVersionUID = -4240592683962044337L;

    /**
     * 有赞订单号
     */
    @ApiModelProperty(value = "有赞订单号")
    private List<String> ids;
}
