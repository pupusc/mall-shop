package com.wanmi.sbc.customer.api.response.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登发送验证码返回值
 * @author: Mr.Tian
 * @create: 2021-01-28 14:57
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengSengCodeResponse  implements Serializable {

    private static final long serialVersionUID = 3800178507626508340L;

    /**
     * true:短信发送成功 false:短信发送失败
     */
    @ApiModelProperty(value = "true:短信发送成功 false:短信发送失败")
    private boolean success;
}
