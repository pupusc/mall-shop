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
 * @description: 调用樊登极验api返回对象
 * @author: Mr.Tian
 * @create: 2021-01-28 14:53
 **/

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengVerifyResponse implements Serializable {
    private static final long serialVersionUID = -7448100173848816692L;

    /**
     * 验证流水号
     */
    @ApiModelProperty(value = "验证流水号")
    private  String challenge;

    /**
     * 验证id
     */
    @ApiModelProperty(value = "验证id")
    private  String gt;
}
