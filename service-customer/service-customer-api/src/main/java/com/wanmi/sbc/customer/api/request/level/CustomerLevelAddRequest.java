package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class CustomerLevelAddRequest implements Serializable {

    private static final long serialVersionUID = 3093772714051985922L;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 等级徽章地址
     */
    @ApiModelProperty(value = "等级徽章地址")
    private String rankBadgeImg;

    /**
     * 所需成长值
     */
    @ApiModelProperty(value = "所需成长值")
    private Long growthValue;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    /**
     * 会员权益id
     */
    @ApiModelProperty(value = "会员权益id")
    private List<Integer> rightsIds;

    /**
     * 当前操作人
     */
    @ApiModelProperty(value = "当前操作人")
    @NotNull
    private String employeeId;
}
