package com.wanmi.sbc.system.response;

import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>查询积分设置信息response</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPointsConfigSimplifyQueryResponse implements Serializable {
    private static final long serialVersionUID = -6431423829720885695L;

    /**
     * 满x积分可用
     */
    @ApiModelProperty(value = "满x积分可用")
    private Long overPointsAvailable;

    /**
     * 积分抵扣限额
     */
    @ApiModelProperty(value = "积分抵扣限额")
    private BigDecimal maxDeductionRate;

    /**
     * 使用方式 0:订单抵扣,1:商品抵扣
     */
    @ApiModelProperty(value = "使用方式 0:订单抵扣,1:商品抵扣")
    private PointsUsageFlag pointsUsageFlag;

    /**
     * 积分价值
     */
    @ApiModelProperty("积分价值")
    private Long pointsWorth;


    /**
     * 是否启用标志 0：停用，1：启用
     */
    @ApiModelProperty(value = "是否启用标志 0：停用，1：启用")
    private EnableStatus status;
}
