package com.wanmi.sbc.erp.api.response;

import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author machaoyang
 * @className QueryTradeResponse
 * @description TODO
 * @date 2021/6/30 下午2:51
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTradeResponse implements Serializable {

    /**
     * 平台单号
     */
    @ApiModelProperty("平台单号")
    private String platformCode;


    /**
     * 发货状态(
     * 0-未发货
     * 1-部分发货
     * 2-全部发货)
     */
    @ApiModelProperty("发货状态")
    private DeliveryStatus deliveryState;
}
