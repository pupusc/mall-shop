package com.wanmi.sbc.marketing.api.request.grouponactivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 批量审核通过拼团活动请求对象
 */
@ApiModel
@Data
public class GrouponActivityBatchCheckRequest implements Serializable {

    private static final long serialVersionUID = -6081625447225022375L;

    /**
     * 批量审核拼团活动
     */
    @ApiModelProperty(value = "批量活动ids")
    @NotNull
    private List<String> grouponActivityIdList;

}
