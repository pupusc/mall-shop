package com.wanmi.sbc.marketing.api.request.grouponcate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>单个查询拼团活动信息表请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponCateByIdsRequest implements Serializable {
    private static final long serialVersionUID = 7383085234146241524L;

    /**
     * 拼团分类Id
     */
    @ApiModelProperty(value = "拼团分类Id")
    private List<String> grouponCateIds;
}