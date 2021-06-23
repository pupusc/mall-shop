package com.wanmi.sbc.marketing.api.request.distributionrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>查询DistributionRecord列表请求参数</p>
 *
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DistributionRecordByCustomerIdRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 会员的Id
     */
    @ApiModelProperty(value = "会员的Id")
    @NotNull
    private String customerId;
}