package com.wanmi.sbc.customer.response;

import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@ApiModel
@Data
public class MyCustomerVO extends DistributionInviteNewForPageVO {

    @ApiModelProperty("客户id")
    private String customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("总计消费金额")
    private BigDecimal amount;

    @ApiModelProperty("订单数量")
    private BigInteger orderNum;
}
