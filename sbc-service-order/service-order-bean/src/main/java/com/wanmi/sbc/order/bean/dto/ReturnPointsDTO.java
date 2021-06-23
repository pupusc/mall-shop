package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 退积分信息
 *
 * @author yang
 * @since 2019/4/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnPointsDTO implements Serializable {
    /**
     * 申请积分
     */
    @ApiModelProperty(value = "申请积分")
    private Long applyPoints;

    /**
     * 实退积分
     */
    @ApiModelProperty(value = "实退积分")
    private Long actualPoints;

}
