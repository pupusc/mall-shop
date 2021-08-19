package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @ClassName CustomerFundsAddAmount
 * @Description 增加账户余额request
 * @Author lvzhenwei
 * @Date 2019/7/16 14:51
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsAddAmountRequest {

    @NotBlank
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    @NotNull
    @ApiModelProperty(value = "余额金额")
    private BigDecimal amount;


    /**
     * 业务编号
     */
    @ApiModelProperty(value = "业务编号")
    private String businessId;

}
