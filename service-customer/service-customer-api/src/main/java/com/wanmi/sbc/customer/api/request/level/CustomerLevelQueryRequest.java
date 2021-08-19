package com.wanmi.sbc.customer.api.request.level;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
public class CustomerLevelQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 8553469382566000431L;

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

    /**
     * 是否是默认 0：否 1：是
     */
    @ApiModelProperty(value = "是否是默认", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isDefault;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;
}
