package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据网关id获取单笔交易记录request</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayByIdRequest extends PayBaseRequest{
    private static final long serialVersionUID = 7160166915904859771L;

    /**
     * 支付/退款对象id
     */
    @ApiModelProperty(value = "支付/退款对象id")
    @NotNull
    private Long gatewayId;


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
