package com.wanmi.sbc.account.bean.vo;

import com.wanmi.sbc.account.bean.enums.SettleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
public class SettlementTotalVO implements Serializable {

    private static final long serialVersionUID = 4931055744540514952L;
    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    BigDecimal totalAmount;

    /**
     * 结算状态
     */
    @ApiModelProperty(value = "结算状态")
    private SettleStatus settleStatus;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
}
