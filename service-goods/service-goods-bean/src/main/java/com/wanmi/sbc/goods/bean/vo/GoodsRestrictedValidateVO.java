package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author baijianhzong
 * @dateTime 2018/11/1 下午4:26
 *
 * 商品限售校验请求的实体
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRestrictedValidateVO implements Serializable {

    private static final long serialVersionUID = -1350115299914313788L;

    /**
     * goodsInfoId
     */
    @ApiModelProperty(value = "goodsInfoId")
    private String skuId;

    /**
     * 购买的数量
     */
    @ApiModelProperty(value = "购买的数量")
    private Long num;
}
