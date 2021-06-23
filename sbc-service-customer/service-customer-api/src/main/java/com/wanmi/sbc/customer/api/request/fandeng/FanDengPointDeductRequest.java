package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登积分扣除接口
 * @author: Mr.Tian
 * @create: 2021-01-28 13:52
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengPointDeductRequest implements Serializable {

    private static final long serialVersionUID = -4710836069854992695L;

    /**
     * 抵扣码 是调用积分锁定接口返回的一个码
     */
    @ApiModelProperty(value = "抵扣码")
    @NotBlank
    private String deductCode;




}
