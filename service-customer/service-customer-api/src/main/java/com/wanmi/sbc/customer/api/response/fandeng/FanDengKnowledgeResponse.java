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
 * @description: 樊登积分返回结果封装类
 * @author: Mr.Tian
 * @create: 2021-01-28 15:02
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengKnowledgeResponse implements Serializable {
    private static final long serialVersionUID = -5689173611940193940L;

    /**
     * 用户当前知豆余额
     */
    @ApiModelProperty(value = "用户当前知豆余额")
    private  Long beans;

}
