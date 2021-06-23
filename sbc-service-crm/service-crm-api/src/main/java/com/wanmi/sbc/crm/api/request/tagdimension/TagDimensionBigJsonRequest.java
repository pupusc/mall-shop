package com.wanmi.sbc.crm.api.request.tagdimension;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionBigJsonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * List
     */
    @ApiModelProperty(value = "dimensionIdList")
    private List<Long> dimensionIdList;
}
