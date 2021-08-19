package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 会员等级参数
 * Created by CHENLI on 2017/4/17.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLevelEditRequest implements Serializable {

    private static final long serialVersionUID = 5284956809221496928L;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    @NotNull
    private String customerLevelName;

    /**
     * 等级徽章地址
     */
    @ApiModelProperty(value = "等级徽章地址")
    @NotNull
    private String rankBadgeImg;

    /**
     * 所需成长值
     */
    @ApiModelProperty(value = "所需成长值")
    @NotNull
    private Long growthValue;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    @NotNull
    private BigDecimal customerLevelDiscount;

    /**
     * 会员权益id
     */
    @ApiModelProperty(value = "会员权益id")
    private List<Integer> rightsIds;

}
