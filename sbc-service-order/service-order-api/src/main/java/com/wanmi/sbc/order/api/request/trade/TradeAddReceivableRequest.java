package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.order.bean.dto.ReceivableAddDTO;
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
 * @Date: 2018-12-06 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAddReceivableRequest implements Serializable {

    /**
     *收款单
     */
    @ApiModelProperty(value = "收款单")
    private ReceivableAddDTO receivableAddDTO;

    /**
     * 平台信息
     */
    @ApiModelProperty(value = "平台信息")
    private Platform platform;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
