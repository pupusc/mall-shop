package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.IsOpen;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>支付网关修改request</p>
 * Created by of628-wenzhi on 2018-08-20-下午2:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayModifyRequest extends GatewayAddRequest{
    private static final long serialVersionUID = -730259769169889802L;

    /**
     * 是否开启: 0关闭 1开启
     */
    @ApiModelProperty(value = "是否开启")
    private IsOpen isOpen = IsOpen.YES;

    @ApiModelProperty(value = "支付网关id")
    @NotNull
    private Long id;
}
