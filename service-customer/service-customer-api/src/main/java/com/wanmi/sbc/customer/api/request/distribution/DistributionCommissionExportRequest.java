package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by of2975 on 2019/4/30.
 */

/**
 * 分销员佣金导出查询参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DistributionCommissionExportRequest extends BaseQueryRequest {

    /**
     * jwt token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 分销员IDList
     */
    @ApiModelProperty(value = "批量查询-分销员List")
    private List<String> distributionIdList;

    /**
     *分销员ID
     */
    @ApiModelProperty(value = "分销员ID")
    private String  distributionId;

    /**
     * 加入起始日期
     */
    @ApiModelProperty(value = "加入起始日期")
    private String createStartTime;

    /**
     * 加入结束日期
     */
    @ApiModelProperty(value = "加入结束日期")
    private String createEndTime;

    /**
     * 加入日期
     */
    @ApiModelProperty(value = "加入日期")
    private String createTime;
}
