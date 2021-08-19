package com.wanmi.sbc.crm.api.request.customgrouprel;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroupRelPageRequest extends BaseQueryRequest {

    /**
     * 人群ids
     */
    @ApiModelProperty(value = "人群ids")
    private List<Long> groupIds;
}
