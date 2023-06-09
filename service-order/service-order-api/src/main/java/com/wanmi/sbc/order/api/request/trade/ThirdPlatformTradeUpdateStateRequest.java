package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.ThirdPlatformTradeUpdateStateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 9:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ThirdPlatformTradeUpdateStateRequest implements Serializable {

    /**
     * 交易单
     */
    @ApiModelProperty(value = "交易单")
    private ThirdPlatformTradeUpdateStateDTO tradeUpdateStateDTO;
}
