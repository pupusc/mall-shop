package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品修改企业价参数
 * 增加虚拟goodsId，表示与其他商品相关类的数据关联
 * @Auth baijianzhong
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEnterPrisePriceDTO implements Serializable {

    private static final long serialVersionUID = -8807674093765870168L;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    private String goodsInfoId;
    
    @ApiModelProperty(value = "spuId")
    private String goodsId;

    /**
     * 企业价格
     */
    @ApiModelProperty(value = "企业价")
    private BigDecimal enterPrisePrice;

    @ApiModelProperty("企业价类型")
    private Integer enterprisePriceType;

}
