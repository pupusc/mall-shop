package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 13:41
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class CustomerLevelDTO {

    @ApiModelProperty(value = "会员等级id")
    private Long customerLevelId;

    @ApiModelProperty(value = "会员等级名称")
    private String customerLevelName;
}
