package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>支付结果</p>
 * Created by of628-wenzhi on 2018-08-14-下午6:37.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResponse implements Serializable {
    private static final long serialVersionUID = 8517553772832540010L;

    /**
     * 返回的支付对象实际为支付凭证，由前端获取后通过JS请求第三方支付
     */
    @ApiModelProperty(value = "返回的支付对象实际为支付凭证，由前端获取后通过JS请求第三方支付")
    private Object object;
}
