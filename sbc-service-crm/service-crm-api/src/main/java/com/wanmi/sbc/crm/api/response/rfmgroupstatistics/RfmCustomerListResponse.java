package com.wanmi.sbc.crm.api.response.rfmgroupstatistics;

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
public class RfmCustomerListResponse {

    /**
     * 会员ids
     */
    @ApiModelProperty(value = "会员ids")
    private List<String> customerIds;
}
