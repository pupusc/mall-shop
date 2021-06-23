package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.customer.bean.dto.CustomerAccountAddOrModifyDTO;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.RefundBillDTO;
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
 * 退单线下退款请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderOfflineRefundRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String rid;

    /**
     * 会员银行账户
     */
    @ApiModelProperty(value = "会员银行账户")
    private CustomerAccountAddOrModifyDTO customerAccount;

    /**
     * 退款流水
     */
    @ApiModelProperty(value = "退款流水")
    @NotNull
    private RefundBillDTO refundBill;

    /**
     * 操作人信息
     */
    @ApiModelProperty(value = "操作人信息")
    @NotNull
    private Operator operator;
}
