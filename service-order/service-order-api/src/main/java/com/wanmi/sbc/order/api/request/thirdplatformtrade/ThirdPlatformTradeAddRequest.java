package com.wanmi.sbc.order.api.request.thirdplatformtrade;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: daiyitian
 * @Description:
 * @Date: 2020-08-20 9:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ThirdPlatformTradeAddRequest implements Serializable {

    /**
     * 订单业务id
     */
    @ApiModelProperty(value = "订单业务id")
    private String businessId;

    @ApiModelProperty(value = "第三方平台类型")
    private ThirdPlatformType thirdPlatformType;
}
