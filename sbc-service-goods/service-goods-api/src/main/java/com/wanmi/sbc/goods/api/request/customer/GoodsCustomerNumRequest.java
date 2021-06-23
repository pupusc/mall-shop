package com.wanmi.sbc.goods.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/11/6 10:08
 * @version: 1.0
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCustomerNumRequest implements Serializable{

    private static final long serialVersionUID = 2884015725025584842L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员编号")
    private String customerId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Long goodsNum;

    /**
     * 叠加标识
     * 1:在原有基础上叠加
     * 默认:全改
     */
    @ApiModelProperty(value = "叠加标识", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addFlag;
}
