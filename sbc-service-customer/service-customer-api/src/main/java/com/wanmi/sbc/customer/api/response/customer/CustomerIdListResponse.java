package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerIdListResponse implements Serializable {
    private static final long serialVersionUID = -7809918666585787044L;

    @ApiModelProperty(value = "会员信息列表")
    private List<String> customerIdList;
}
