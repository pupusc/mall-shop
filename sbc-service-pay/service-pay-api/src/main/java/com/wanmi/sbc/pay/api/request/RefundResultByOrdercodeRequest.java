package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据订单号和退单号查询退款结果请求参数</p>
 * Created by of628-wenzhi on 2018-08-18-下午2:31.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResultByOrdercodeRequest extends PayBaseRequest {

    private static final long serialVersionUID = -1958075254054555250L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @NotBlank
    private String orderCode;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    @NotBlank
    private String returnOrderCode;
}
