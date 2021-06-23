package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoModifyDistributionGoodsAuditRequest implements Serializable {

    /**
     * 批量SkuID
     */
    @ApiModelProperty(value = "批量SkuID")
    @NonNull
    private List<String> goodsInfoIds;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @ApiModelProperty(value = "分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private Integer distributionGoodsAudit;


    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private String distributionGoodsAuditReason;

}
