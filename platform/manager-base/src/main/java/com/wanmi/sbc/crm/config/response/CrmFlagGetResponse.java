package com.wanmi.sbc.crm.config.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmFlagGetResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Crm标记 true:是false:否
     */
    @ApiModelProperty(value = "Crm标记 true:是false:否")
    private Boolean crmFlag;

}
