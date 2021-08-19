package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNotRelatedListResponse implements Serializable {
    private static final long serialVersionUID = 5150195226452137408L;

    @ApiModelProperty(value = "会员信息列表")
    private List<Map<String, Object>> customers;
}
