package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员资金-根据会员ID查询对象
 * @author: yangzhen
 * @createDate: 2020/12/16 11:06
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsByCustomerIdListRequest implements Serializable {

    /**
     * 会员Id
     */
    @ApiModelProperty(value = "会员Id")
    private List<String> customerIds;
}
