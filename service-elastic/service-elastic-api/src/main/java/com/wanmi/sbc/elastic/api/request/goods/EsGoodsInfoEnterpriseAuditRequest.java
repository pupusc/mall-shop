package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class EsGoodsInfoEnterpriseAuditRequest implements Serializable {

    /**
     * 批量SkuID
     */
    @ApiModelProperty(value = "批量SkuID")
    @NonNull
    private List<String> goodsInfoIds;

    /**
     * 企业购商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过
     */
    @ApiModelProperty(value = "企业购商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private EnterpriseAuditState enterPriseAuditStatus;


    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private String enterPriseGoodsAuditReason;

}
