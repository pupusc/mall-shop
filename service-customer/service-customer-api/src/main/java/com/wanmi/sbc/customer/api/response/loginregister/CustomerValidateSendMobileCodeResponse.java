package com.wanmi.sbc.customer.api.response.loginregister;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:18
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerValidateSendMobileCodeResponse implements Serializable {
    private static final long serialVersionUID = -2531694742720407955L;

    @ApiModelProperty(value = "是否可以发送验证码")
    private Boolean result;
}
