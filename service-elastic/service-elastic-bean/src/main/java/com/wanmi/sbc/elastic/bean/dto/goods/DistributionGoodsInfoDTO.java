package com.wanmi.sbc.elastic.bean.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Geek Wang
 * @createDate: 2019/2/28 18:26
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DistributionGoodsInfoDTO implements Serializable {

    /**
     * SkuId
     */
    @ApiModelProperty(value = "SkuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    @NotNull
    private BigDecimal distributionCommission;
}
