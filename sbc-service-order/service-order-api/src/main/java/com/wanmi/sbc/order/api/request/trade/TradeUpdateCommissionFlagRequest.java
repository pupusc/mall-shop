package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:
 * @Autho qiaokang
 * @Date：2019-03-14 19:24:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeUpdateCommissionFlagRequest implements Serializable {

    private static final long serialVersionUID = -4667846795763256425L;

    /**
     * 交易号
     */
    @ApiModelProperty(value = "交易号")
    @NotBlank
    private String tradeId;

    /**
     * 是否返利
     */
    @ApiModelProperty(value = "是否返利")
    @NotNull
    private Boolean commissionFlag;

}
