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
 * @description: 樊登积分消费接口返回值，扣减 锁定共用
 * @author: Mr.Tian
 * @create: 2021-01-28 15:06
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengLockResponse implements Serializable {
    private static final long serialVersionUID = -6929746249766686228L;
    /**
     * 抵扣码
     */
    @ApiModelProperty(value = "抵扣码")
    private String deductionCode;

}

