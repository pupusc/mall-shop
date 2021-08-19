package com.wanmi.sbc.customer.api.response.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
    调用樊登前置接口返回值
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengPrepositionResponse implements Serializable {

    private static final long serialVersionUID = -1443576684409931917L;

    /**
     * 是否需要调用极验api true = 需要 false = 不需要
     */
    @ApiModelProperty(value = "是否需要调用极验api true = 需要 false = 不需要")
    private boolean needGeetest;

    /**
     * 用于发送验证码时的验证token
     */
    @ApiModelProperty(value = "用于发送验证码时的验证token")
    private String token;
}
