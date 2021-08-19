package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户等级
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
public class CustomerLevelBaseVO implements Serializable {

    private static final long serialVersionUID = 964213406533479019L;

    /**
     * 客户等级ID
     */

    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;
}
