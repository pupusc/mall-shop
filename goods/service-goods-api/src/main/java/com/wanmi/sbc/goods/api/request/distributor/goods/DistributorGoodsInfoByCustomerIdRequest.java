package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据会员Id查询条件
 *
 * @author liutao
 * @date 2019/4/12 3:31 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributorGoodsInfoByCustomerIdRequest implements Serializable {

    /**
     * 会员Id
     */
    @ApiModelProperty(value = "会员Id")
    private String customerId;
}
