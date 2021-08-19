package com.wanmi.sbc.setting.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class SystemPointsConfigQueryResponse implements Serializable {
    private static final long serialVersionUID = -6431423829720885695L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String pointsConfigId;

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
     * 积分过期月份
     */
    @ApiModelProperty(value = "积分过期月份")
    private Integer pointsExpireMonth;

    /**
     * 积分过期日期
     */
    @ApiModelProperty(value = "积分过期日期")
    private Integer pointsExpireDay;

    /**
     * 积分说明
     */
    @ApiModelProperty(value = "积分说明")
    private String remark;

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

    /**
     * 是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否删除标志 0：否，1：是")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
}
