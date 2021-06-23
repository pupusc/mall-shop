package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>退款请求返回结构</p>
 * Created by of628-wenzhi on 2018-08-18-下午2:36.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse implements Serializable {
    private static final long serialVersionUID = -2216887857123412426L;

    /**
     * 退款成功后返回的退款对象
     */
    @ApiModelProperty(value = "退款成功后返回的退款对象")
    private Object object;
}
