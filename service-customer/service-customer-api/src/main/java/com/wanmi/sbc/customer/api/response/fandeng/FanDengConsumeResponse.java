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
 * @description: 樊登扣减，返回积分调用返回值
 * @author: Mr.Tian
 * @create: 2021-01-28 15:10
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengConsumeResponse implements Serializable {

    private static final long serialVersionUID = -4391625089205205807L;
    /**
     * 成功标识 true：成功 false:失败
     */
    @ApiModelProperty(value = "成功标识 true：成功 false:失败")
    private Boolean succFlag;
}
