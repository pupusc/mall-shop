package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退单修改退单价格请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderOnlineModifyPriceRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 退单信息
     */
    @ApiModelProperty(value = "退单信息")
    @NotNull
    private ReturnOrderDTO returnOrder;

    /**
     * 退款评论
     */
    @ApiModelProperty(value = "退款评论")
    private String refundComment;

    /**
     * 实退金额
     */
    @ApiModelProperty(value = "实退金额")
    private BigDecimal actualReturnPrice;

    /**
     * 实退积分
     */
    @ApiModelProperty(value = "实退积分")
    private Long actualReturnPoints;

    /**
     * 操作人信息
     */
    @ApiModelProperty(value = "操作人信息")
    @NotNull
    private Operator operator;
}
