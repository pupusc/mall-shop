package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DistributionGoodsInfoModifyDTO
 * 分销商品信息传输对象
 * @author chenli
 * @dateTime 2018/11/6 下午2:29
 */
@ApiModel
@Data
public class DistributionGoodsInfoModifyDTO implements Serializable {

    private static final long serialVersionUID = -5266737458614077235L;

    /**
     * 商品SKU编号
     */
    @NotBlank
    @ApiModelProperty(value = "商品SKU编号")
    private String goodsInfoId;


    /**
     * 佣金比例
     */
    @NotNull
    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate;

    /**
     * 分销佣金
     */
    @NotNull
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal distributionCommission;


}