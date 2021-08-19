package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDelFlagGetResponse implements Serializable {
    private static final long serialVersionUID = 5150195226452137408L;

    @ApiModelProperty(value = "删除标记")
    private Boolean delFlag;
}
