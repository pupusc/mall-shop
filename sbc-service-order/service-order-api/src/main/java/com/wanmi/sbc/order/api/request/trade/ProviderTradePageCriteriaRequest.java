package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.ProviderTradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: qiaokang
 * @Description:
 * @Date: 2020-03-27 13:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ProviderTradePageCriteriaRequest implements Serializable {

    /**
     * 是否是可退单查询
     */
    @ApiModelProperty(value = "是否是可退单查询",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean isReturn;

    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数")
    private ProviderTradeQueryDTO tradePageDTO;
}
