package com.wanmi.sbc.customer.api.request.customer;

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
@AllArgsConstructor
@NoArgsConstructor
public class CustomerYzUidListRequest implements Serializable {

    private static final long serialVersionUID = 7343715116629124901L;
    @ApiModelProperty(value = "有赞uid")
    private List<Long> yzUids;
}
