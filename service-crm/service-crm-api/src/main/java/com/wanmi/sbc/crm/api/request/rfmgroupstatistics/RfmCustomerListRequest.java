package com.wanmi.sbc.crm.api.request.rfmgroupstatistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmCustomerListRequest implements Serializable {

    private static final long serialVersionUID = 506885044586397225L;
    /**
     * 人群ids
     */
    @ApiModelProperty(value = "人群ids")
    private List<Long> groupIds;
}
