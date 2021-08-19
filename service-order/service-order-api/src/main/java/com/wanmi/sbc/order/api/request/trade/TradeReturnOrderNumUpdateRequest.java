package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:40 2019/4/13
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeReturnOrderNumUpdateRequest {

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 是否增加退单数量 true：增加，false：减少
     */
    @ApiModelProperty(value = "是否增加退单数量")
    private boolean addFlag;

}
