package com.wanmi.sbc.account.bean.dto;

import com.wanmi.sbc.account.bean.enums.ReturnStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hht on 2017/12/7.
 */
@ApiModel
@Data
@Builder
public class SettleReturnDTO implements Serializable{

    private static final long serialVersionUID = -4677097862452708442L;

    /**
     * 退单状态
     */
    @ApiModelProperty(value = "退单状态")
    private ReturnStatus returnStatus;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量")
    private long returnNum;

    /**
     * 退单的应退均摊价
     */
    @ApiModelProperty(value = "退单的应退均摊价")
    private BigDecimal splitReturnPrice;
}
