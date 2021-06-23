package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:25 2019/6/13
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelVO implements Serializable {

    private static final long serialVersionUID = 5526317333802860719L;

    /**
     * 分销员等级id
     */
    @ApiModelProperty("分销员等级id")
    private String distributorLevelId;

    /**
     * 分销员等级名称
     */
    @ApiModelProperty("分销员等级名称")
    private String distributorLevelName;

    /**
     * 佣金比例
     */
    @ApiModelProperty("佣金比例")
    private BigDecimal commissionRate;

    /**
     * 佣金提成比例
     */
    @ApiModelProperty("佣金提成比例")
    private BigDecimal percentageRate;

    /**
     * 销售额门槛是否开启
     */
    @ApiModelProperty("销售额门槛是否开启")
    @NotNull
    private DefaultFlag salesFlag;

    /**
     * 销售额门槛
     */
    @ApiModelProperty("销售额门槛")
    private BigDecimal salesThreshold;

    /**
     * 到账收益额门槛是否开启
     */
    @ApiModelProperty("到账收益额门槛是否开启")
    @NotNull
    private DefaultFlag recordFlag;

    /**
     * 到账收益额门槛
     */
    @ApiModelProperty("到账收益额门槛")
    private BigDecimal recordThreshold;

    /**
     * 邀请人数门槛是否开启
     */
    @ApiModelProperty("邀请人数门槛是否开启")
    @NotNull
    private DefaultFlag inviteFlag;

    /**
     * 邀请人数门槛
     */
    @ApiModelProperty("邀请人数门槛")
    private Integer inviteThreshold;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updatePerson;

    /**
     * 等级排序
     */
    @ApiModelProperty("等级排序")
    private Integer sort;

}
