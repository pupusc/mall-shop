package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据订单号查询支付结果请求参数</p>
 * Created by of628-wenzhi on 2018-08-18-下午2:31.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResultByOrdercodeRequest extends PayBaseRequest{
    private static final long serialVersionUID = -2335691701546213080L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @NotBlank
    private String orderCode;
}
