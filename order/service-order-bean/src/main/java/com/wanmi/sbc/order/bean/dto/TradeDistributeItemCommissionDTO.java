package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:47 2019/6/21
 * @Description: 分销单品佣金信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TradeDistributeItemCommissionDTO extends BaseRequest {

    private static final long serialVersionUID = -5007288350277079667L;

    /**
     * 提成人会员id
     */
    @ApiModelProperty(value = "提成人会员id")
    private String customerId;

    /**
     * 提成人分销员id
     */
    @ApiModelProperty(value = "提成人分销员id")
    private String distributorId;

    /**
     * 提成人总佣金
     */
    @ApiModelProperty(value = "提成人总佣金")
    private BigDecimal commission;

}
