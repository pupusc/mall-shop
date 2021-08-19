package com.wanmi.sbc.order.api.response.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 重复的物流单号
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class LogisticNoRepeatResponse implements Serializable {

    /**
     * 交易单
     */
    @ApiModelProperty(value = "交易单列表")
    private List<String> logisticNoList;
}
