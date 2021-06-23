package com.wanmi.sbc.elastic.api.request.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 批量审核通过拼团活动请求对象
 * @author houshuai
 */
@ApiModel
@Data
public class EsGrouponActivityBatchCheckRequest implements Serializable {

    private static final long serialVersionUID = -6072426645095605718L;
    /**
     * 批量审核拼团活动
     */
    @ApiModelProperty(value = "批量活动ids")
    @NotNull
    private List<String> grouponActivityIdList;

}