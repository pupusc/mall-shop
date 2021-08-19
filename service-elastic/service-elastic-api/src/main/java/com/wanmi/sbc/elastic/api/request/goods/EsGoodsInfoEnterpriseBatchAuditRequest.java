package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.dto.BatchEnterPrisePriceDTO;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class EsGoodsInfoEnterpriseBatchAuditRequest implements Serializable {


    @ApiModelProperty(value = "商品修改企业价参数")
    private List<BatchEnterPrisePriceDTO> batchEnterPrisePriceDTOS;

    @ApiModelProperty(value = "企业购审核状态")
    private EnterpriseAuditState enterpriseAuditState;

}
