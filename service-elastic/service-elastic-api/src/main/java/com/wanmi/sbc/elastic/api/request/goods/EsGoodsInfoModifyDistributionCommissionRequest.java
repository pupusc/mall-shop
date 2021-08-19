package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoModifyDistributionCommissionRequest implements Serializable {

    /**
     * 批量SkuID和分销佣金
     */
    @ApiModelProperty(value = "批量SkuID和佣金比例")
    private List<DistributionGoodsInfoModifyDTO> distributionGoodsInfoDTOList;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @ApiModelProperty(value = "分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private DistributionGoodsAudit distributionGoodsAudit;


    /**
     * 分销商品状态，配合分销开关使用
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer distributionGoodsStatus;


}
