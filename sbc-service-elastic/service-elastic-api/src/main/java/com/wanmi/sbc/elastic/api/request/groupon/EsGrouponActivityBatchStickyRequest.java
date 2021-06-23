package com.wanmi.sbc.elastic.api.request.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/22 10:24
 * @description <p> </p>
 */
/**
 * 批量精选拼团活动请求对象
 */
@ApiModel
@Data
public class EsGrouponActivityBatchStickyRequest implements Serializable {

    private static final long serialVersionUID = 1864692896314497154L;
    /**
     * 批量精选拼团活动
     */
    @ApiModelProperty(value = "批量活动ids")
    @NotNull
    private List<String> grouponActivityIdList;

    /**
     * 是否精选
     */
    @ApiModelProperty(value = "是否精选")
    @NotNull
    private  Boolean sticky;
}
