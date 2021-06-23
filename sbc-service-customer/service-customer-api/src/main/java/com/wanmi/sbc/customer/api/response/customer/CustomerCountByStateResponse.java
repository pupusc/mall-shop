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
public class CustomerCountByStateResponse implements Serializable {
    private static final long serialVersionUID = 5150195226452137408L;

    @ApiModelProperty(value = "总数")
    private long count;
}
